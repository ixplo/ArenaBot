package ml.ixplo.arenabot.user.params;

import java.lang.reflect.Field;
import java.util.List;

public class Params {
    private List<Field> params;

    public List<Field> getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Params params1 = (Params) o;

        return params != null ? params.equals(params1.params) : params1.params == null;
    }

    @Override
    public int hashCode() {
        return params != null ? params.hashCode() : 0;
    }
}
