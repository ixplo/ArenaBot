package ml.ixplo.arenabot.user;

public interface IUser {
    Integer getUserId();
    String getName();
    String getTeamId();
    void setTeamId(String id);
}
