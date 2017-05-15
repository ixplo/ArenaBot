package arenabot.commands;

import arenabot.Messages;
import arenabot.users.ArenaUser;
import arenabot.battle.Battle;
import arenabot.battle.Round;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * ixplo
 * 26.04.2017.
 */
public class CmdDo extends BotCommand {
    public static final String LOGTAG = "EXCOMMAND";

    public CmdDo() {
        super("do", "&lt a,p,h,m &gt &ltномер&gt &lt%&gt - действовать на цель под указанным номером");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        int percent;
        String spellId ="";
        if (!chat.isUserChat()) {
            return;
        }
        if (!ArenaUser.doesUserExists(user.getId())) {
            return;
        }
        if (ArenaUser.getStatus(user.getId()) != 2) {
            return;
        }
        if(strings.length < 3){
            percent = 100;

        }else {
            percent = Integer.parseInt(strings[2]);
        }
        if (percent > 100) {//todo все в месседжи
            SendMessage msg = new SendMessage();
            msg.setChatId(chat.getId());
            msg.enableHtml(true);
            msg.setText("Больше 100% быть не может. Инфа 146%!");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!isValid(absSender, user, chat.getId(), strings)) {
            return;
        }
        if (strings[0].equals(ArenaUser.UserClass.MAGE.toString()) && strings[3] != null){
            if(ArenaUser.getUser(user.getId()).getUserClass().equals(ArenaUser.UserClass.ARCHER.toString()) ||
                    ArenaUser.getUser(user.getId()).getUserClass().equals(ArenaUser.UserClass.WARRIOR.toString())){//todo убрать эту срань
                Messages.sendMessage(absSender,chat.getId(),"Доступно только магам и жрецам");
                return;
            }
            spellId = strings[3];
        }else if(strings[0].equals(ArenaUser.UserClass.MAGE.toString())){
            Messages.sendMessage(absSender,chat.getId(),"Неверный формат заклинания");
            return;
        }
        Messages.sendDoMsg(absSender, chat.getId(), strings, percent);//todo перенести в takeAction
        Battle.battle.interrupt();
        Round.round.takeAction(user.getId(), strings[0], Integer.parseInt(strings[1]), percent, spellId);
    }
//todo проверки выделить в отдельные методы и кидать исключения
    //todo вторая часть кода выделяется в другой метод и выполняется, если прошли проверку в первых методах
    private boolean isValid(AbsSender absSender, User user, Long chatId, String[] strings) {//todo перенести в takeAction
        int target = Integer.parseInt(strings[1]);
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableHtml(true);

        if (strings.length == 0) {
            msg.setText("Формат: <i>/do a 1 100</i> - атаковать цель под номером 1 на 100%");
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return false;
        }
        if (target > Round.getCurMembersId().size()) {
            msg.setText("Цель по номером " + Integer.parseInt(strings[1]) +
                    " не найдена. Всего есть целей: " + Round.getCurMembersId().size());
            try {
                absSender.sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
