package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * ixplo
 * 29.04.2017.
 */
public class Round {
    public static Round round;
    private static List<Integer> curMembersId;
    private static List<String> curTeamsId;
    private List<? extends IUser> members;
    private List<Team> teams;
    private List<Order> orders;
    Battle battle;

    public static List<Integer> getCurMembersId() {
        return curMembersId;
    }

    public List<? extends IUser> getMembers() {
        return members;
    }

    //todo убрать лишние параметры
    //todo не изменять передаваемые параметры
    Round(List<Integer> curMembersId, List<String> curTeamsId, List<? extends IUser> members, List<Team> teams, Battle battle) {
        this.members = members;
        this.teams = teams;
        this.battle = battle;
        Round.curMembersId = curMembersId;
        Round.curTeamsId = curTeamsId;
        Round.round = this;
        orders = new ArrayList<>();
    }

    void startRound() {
        Timer timer = new Timer();
        timer.schedule(new EndRound(this), Config.ROUND_TIME);
        timer.schedule(new RemindAboutEndRound(this), Config.ROUND_REMIND);
        Messages.sendListToAll(teams);
        for (IUser member : members) {
            orders.add(new Order(member.getUserId(), round));
        }
        while (!isOrdersDone()) {
            try {
                Thread.sleep(Config.ROUND_TIME);
            } catch (InterruptedException e) {
                System.out.println("Получен заказ");
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

    private void takeOutCorpses() {
        for (IUser member : members) {
            if (member instanceof ArenaUser) {
                ArenaUser arenaUser = (ArenaUser) member;
                if (arenaUser.getCurHitPoints() <= 0) {
                    Messages.sendToAll(members, "<b>" + arenaUser.getName() + "</b> потерял возможность продолжать бой.");
                    curMembersId.remove(getIndex(curMembersId, member.getUserId()));
                    Team.refreshTeamsId(members, curMembersId, curTeamsId);
                }
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
        throw new RuntimeException("Invalid userId: " + userId);
    }

    private int getIndex(List<Integer> users, int userId) {
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (users.get(i) == (userId)) {
                return i;
            }
        }
        throw new RuntimeException("Invalid userId: " + userId);
    }

    private int getIndex(List<Team> teams, String teamId) {
        int size = teams.size();
        for (int i = 0; i < size; i++) {
            if (teams.get(i).getId().equals(teamId)) {
                return i;
            }
        }
        throw new RuntimeException("Invalid teamId: " + teamId);
    }

    public int getIndex(int userId) {
        int size = members.size();
        for (int i = 0; i < size; i++) {
            if (members.get(i).getUserId() == userId) {
                return i;
            }
        }
        throw new RuntimeException("Invalid userId: " + userId);
    }

    public IUser getMember(Integer userId) {
        for (IUser member : members) {
            if (member.getUserId().equals(userId)) {
                return member;
            }
        }
        throw new RuntimeException("Invalid userId: " + userId);
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
            if(action.getActionId().equals("a")){
                attackOnTarget.add(action);
            }
        }
        return attackOnTarget;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
