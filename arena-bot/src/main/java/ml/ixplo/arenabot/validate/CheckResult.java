package ml.ixplo.arenabot.validate;

public class CheckResult {
    private boolean result;
    private String errorMessage;

    public CheckResult(boolean result) {
        this.result = result;
    }

    public CheckResult(String errorMessage) {
        this.result = false;
        this.errorMessage = errorMessage;
    }

    public boolean isGood() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
