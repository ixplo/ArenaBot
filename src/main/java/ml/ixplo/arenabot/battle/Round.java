package ml.ixplo.arenabot.battle;

import ml.ixplo.arenabot.battle.actions.Action;
import ml.ixplo.arenabot.config.Config;
import ml.ixplo.arenabot.config.PropertiesLoader;
import ml.ixplo.arenabot.exception.ArenaUserException;
import ml.ixplo.arenabot.messages.Messages;
import ml.ixplo.arenabot.user.ArenaUser;
import ml.ixplo.arenabot.user.IUser;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;

/**
 * ixplo
 * 29.04.2017.
 */
public class Round {
    public static final String LOGTAG = "ROUND";
    private static Round current;
    private List<Integer> curMembersId;
    private Set<String> curTeamsId;
    private List<? extends IUser> members;
    private List<Team> teams;
    private List<Order> orders;

    public static Round getCurrent() {
        return current;
    }

    public static BattleState execute(BattleState battleState) {
        current = new Round(battleState);
        return current.execute();
    }

    //todo members, curMembers, teamsId - не нужны. А нужны просто Teams (и в них можно хранить начальное состояние)
    Round(BattleState battleState) {
        this.members = new ArrayList<>(battleState.getMembers());
        this.teams = new ArrayList<>(battleState.getTeams());
        this.curMembersId = new ArrayList<>(battleState.getCurMembersId());
        this.curTeamsId = new HashSet<>(battleState.getCurTeamsId());
        orders = new ArrayList<>();
        for (IUser member : members) {
            orders.add(new Order(member.getUserId(), this));
        }
    }

    private BattleState execute() {
        Timer timer = startTimer();
        Messages.sendListToAll(teams);
        waitForAllOrdersAccepted();
        timer.cancel();
        handleActions();
        printResults();
        takeOutCorpses();
        return getBattleState();
    }

    private Timer startTimer() {
        Timer timer = new Timer();
        timer.schedule(new EndRound(this), PropertiesLoader.getInstance().getLong(Config.ROUND_DURATION));
        timer.schedule(new RemindAboutEndOfRound(this), PropertiesLoader.getInstance().getLong(Config.END_OF_ROUND_REMIND));
        return timer;
    }

    private void waitForAllOrdersAccepted() {
        while (!isOrdersDone()) {
            try {
                Thread.sleep(Config.DELAY_TIME);
            } catch (InterruptedException e) {
                //no-op
                BotLogger.info(LOGTAG, "Получен заказ");
            }
        }
    }

    private void printResults() {
        Messages.sendToAll(members, "<b>Результаты раунда:</b>");
        for (Order order : orders) {
            for (Action action : order.getActions()) {
                if (action.getMessage() != null) {
                    Messages.sendToAll(members, action.getMessage());
                }
            }
        }
    }

    private void handleActions() {
        Queue<Action> priorityQueue = new PriorityQueue<>();
        for (Order order : orders) {
            priorityQueue.addAll(order.getActions());
        }
        while (!priorityQueue.isEmpty()) {
            Action action = priorityQueue.poll();
            action.doAction();
        }
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
                    curTeamsId = Team.getTeamsId(members, curMembersId);
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

    public void takeAction(Action action) {
        int index = getIndex(orders, action.getUser().getUserId());
        orders.get(index).addAction(action);
    }

    private int getIndex(List<Order> orders, Integer userId) {
        int size = orders.size();
        for (int i = 0; i < size; i++) {
            if (orders.get(i).userId.equals(userId)) {
                return i;
            }
        }
        throw new ArenaUserException(Config.INVALID_USER_ID + userId);
    }

    private int getIndex(List<Integer> users, int userId) {
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if (users.get(i) == (userId)) {
                return i;
            }
        }
        throw new ArenaUserException(Config.INVALID_USER_ID + userId);
    }

    public int getIndex(int userId) {
        int size = members.size();
        for (int i = 0; i < size; i++) {
            if (members.get(i).getUserId() == userId) {
                return i;
            }
        }
        throw new ArenaUserException(Config.INVALID_USER_ID + userId);
    }

    public IUser getMember(Integer userId) {
        for (IUser member : members) {
            if (member.getUserId().equals(userId)) {
                return member;
            }
        }
        throw new ArenaUserException(Config.INVALID_USER_ID + userId);
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

    public BattleState getBattleState() {
        BattleState battleState = new BattleState();
        battleState.setMembers((List<ArenaUser>) members);
        battleState.setCurMembersId(curMembersId);
        battleState.setTeams(teams);
        battleState.setCurTeamsId(new ArrayList<>(curTeamsId));
        return battleState;
    }

    @Override
    public String toString() {
        return "Round{" +
                "curMembersId=" + curMembersId +
                ", curTeamsId=" + curTeamsId +
                '}';
    }
}
