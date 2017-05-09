package arenabot.battle;

import arenabot.users.ArenaUser;
import arenabot.Config;
import arenabot.Messages;
import arenabot.battle.actions.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ixplo
 * 29.04.2017.
 */
public class Round {
    public static Round round;
    private static List<Integer> curMembersId;
    private static List<String> curTeamsId;
    private List<ArenaUser> members;
    private List<Team> teams;
    private List<Order> orders;
    Battle battle;

    public static List<Integer> getCurMembersId() {
        return curMembersId;
    }

    //todo убрать лишние параметры!!!
    //todo не изменять передаваемые параметры!!!
    Round(List<Integer> curMembersId, List<String> curTeamsId, List<ArenaUser> members, List<Team> teams, Battle battle) {
        this.members = members;
        this.teams = teams;
        this.battle = battle;
        Round.curMembersId = curMembersId;
        Round.curTeamsId = curTeamsId;
        Round.round = this;
        orders = new ArrayList<>();
    }

    void startRound() {
        Messages.sendListToAll(teams);
        for (ArenaUser member : members) {
            orders.add(new Order(member.getUserId(), round));
        }
        while (!isOrdersDone()) {//todo доделать конец раунда по delay
            try {
                TimeUnit.SECONDS.sleep(Config.ROUND_DELAY);
            } catch (InterruptedException e) {
                System.out.println("Получен заказ");
            }
        }
        Messages.sendToAll(members, "<b>Результаты раунда:</b>");
        for (Order order : orders) {
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
        for (ArenaUser member : members) {
            if (member.getCurHitPoints() <= 0) {
                Messages.sendToAll(members, "<b>" + member.getName() + "</b> потерял возможность продолжать бой.");
                curMembersId.remove(getIndex(curMembersId, member.getUserId()));
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

    public void takeAction(int userId, String actionId, int target, int percent) {
        int index = getIndex(orders, userId);
        orders.get(index).addAction(Action.create(userId, actionId, members.get(target - 1).getUserId(), percent));
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

    public ArenaUser getMember(Integer userId) {
        for (ArenaUser member : members) {
            if (member.getUserId() == userId) {
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
}
