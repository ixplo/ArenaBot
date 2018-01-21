package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * ixplo
 * 29.04.2017.
 */
public class Round {
    public static final String LOGTAG = "MESSAGES";
    private static Round round;
    private List<Integer> curMembersId;
    private List<String> curTeamsId;
    private List<? extends IUser> members;
    private List<Team> teams;
    private List<Order> orders;


    //todo убрать лишние параметры
    //todo не изменять передаваемые параметры
    Round(List<Integer> curMembersId, List<String> curTeamsId, List<? extends IUser> members, List<Team> teams) {
        this.members = new ArrayList<>(members);
        this.teams = new ArrayList<>(teams);
        this.curMembersId = new ArrayList<>(curMembersId);
        this.curTeamsId = new ArrayList<>(curTeamsId);
        orders = new ArrayList<>();
        round = this;
    }

    public static Round getCurrent() {
        return round;
    }

    void begin() {
        Timer timer = new Timer();
        timer.schedule(new EndRound(this), Config.ROUND_TIME);
        timer.schedule(new RemindAboutEndRound(this), Config.ROUND_REMIND);
        Messages.sendListToAll(teams);
        for (IUser member : members) {
            orders.add(new Order(member.getUserId(), round));
        }
        while (!isOrdersDone()) {
            try {
                Thread.sleep(Config.DELAY_TIME);
            } catch (InterruptedException e) {
                //no-op
                BotLogger.info(LOGTAG, "Получен заказ");
            }
        }
        timer.cancel();
        Messages.sendToAll(members, "<b>Результаты раунда:</b>");
        for (Order order : orders) { //todo queue with priority 1.attack 2.protect 3.magic 4.heal
            for (Action action : order.getActions()) {
                action.doAction();
            }
        }
        for (Order order : orders) {
            for (Action action : order.getActions()) {
                if (action.getMessage() != null) {
                    Messages.sendToAll(members, action.getMessage());
                }
            }
        }
        takeOutCorpses();

    }

    public void stop() {
        for (Order order : orders) {
            order.setZeroCommonPercent();
        }
    }

    private void takeOutCorpses() {
        for (IUser member : members) {
                ArenaUser arenaUser = ArenaUser.getUser(member.getUserId());
                if (arenaUser.getCurHitPoints() <= 0) {
                    Messages.sendToAll(members, "<b>" + arenaUser.getName() + "</b> потерял возможность продолжать бой.");
                    curMembersId.remove(getIndex(curMembersId, arenaUser.getUserId()));
                    Team.refreshTeamsId(members, curMembersId, curTeamsId);
                }
        }
    }

    private boolean isOrdersDone() {
        for (Order order : orders) {
            if (order.getCommonPercent() != 0) {
                return false;
            }
        }
        return true;
    }

    public void takeAction(int userId, String actionId, int targetId, int percent, String spellId) {
        int index = getIndex(orders, userId);
        orders.get(index).addAction(Action.create(userId, actionId, targetId, percent, spellId));
    }

    private int getIndex(List<Order> orders, Integer userId) {
        int size = orders.size();
        for (int i = 0; i < size; i++) {
            if (orders.get(i).userId.equals(userId)) {
                return i;
            }
        }
        throw new ArenaUserException("Invalid userId: " + userId);
    }

    private int getIndex(List<Integer> users, int userId) {
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (users.get(i) == (userId)) {
                return i;
            }
        }
        throw new ArenaUserException("Invalid userId: " + userId);
    }

    private int getIndex(List<Team> teams, String teamId) {
        int size = teams.size();
        for (int i = 0; i < size; i++) {
            if (teams.get(i).getId().equals(teamId)) {
                return i;
            }
        }
        throw new ArenaUserException("Invalid teamId: " + teamId);
    }

    public int getIndex(int userId) {
        int size = members.size();
        for (int i = 0; i < size; i++) {
            if (members.get(i).getUserId() == userId) {
                return i;
            }
        }
        throw new ArenaUserException("Invalid userId: " + userId);
    }

    public IUser getMember(Integer userId) {
        for (IUser member : members) {
            if (member.getUserId().equals(userId)) {
                return member;
            }
        }
        throw new ArenaUserException("Invalid userId: " + userId);
    }

    public List<Action> getActionsByTarget(int targetId) {
        List<Action> result = new ArrayList<>();
        for (Order order : orders) {
            for (Action action : order.getActions()) {
                if (action.getTarget().getUserId() == targetId) {
                    result.add(action);
                }
            }
        }
        return result;
    }

    public List<Action> getAttackOnTargetList(int targetId){
        List<Action> attackOnTarget = new ArrayList<>();
        List<Action> onTargetList = getActionsByTarget(targetId);
        for (Action action : onTargetList) {
            if(action.getActionType().equals("a")){
                attackOnTarget.add(action);
            }
        }
        return attackOnTarget;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Integer> getCurMembersId() {
        return curMembersId;
    }

    public List<IUser> getMembers() {
        return (List<IUser>) members;
    }

}
