{#- .name: Java class exposing immutable list -#}
{#- .description: Uses unmodifiableList helper function to return unmodifiable view of an ArrayList -#}
{#- .default(class_name): Container -#}
{#- .default(item_name): name -#}
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class {{class_name}}
{
    public {{class_name}}()
    {
    }

    public void add{{item_name | capitalize}}(String {{item_name}})
    {
        _{{item_name | pluralize}}.add({{item_name}});
    }

    public List<String> {{item_name | pluralize}}()
    {
        return _{{item_name | pluralize}}Immutable;
    }

    private ArrayList<String> _{{item_name | pluralize}} = new ArrayList<String>();
    private List<String> _{{item_name | pluralize}}Immutable = Collections.unmodifiableList(_{{item_name | pluralize}}s);
}
