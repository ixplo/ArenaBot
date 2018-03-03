package ml.ixplo.arenabot.user.params;

import java.lang.reflect.Field;
import java.util.List;

public class Params {
    private List<Field> fields;

    public List<Field> get() {
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Params params1 = (Params) o;

        return fields != null ? fields.equals(params1.fields) : params1.fields == null;
    }

    @Override
    public int hashCode() {
        return fields != null ? fields.hashCode() : 0;
    }
}
