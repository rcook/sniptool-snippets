{#- .name: Java class exposing immutable list -#}
{#- .description: Uses unmodifiableList helper function to return unmodifiable view of an ArrayList -#}
{#- .default(class_name): Container -#}
{#- .default(item_plural): names -#}
{#- .default(item_singular): name -#}
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class {{class_name}}
{
    public {{class_name}}()
    {
    }

    public void addName(String {{item_singular}})
    {
        _{{item_plural}}.add({{item_singular}});
    }

    public List<String> {{item_plural}}()
    {
        return _{{item_plural}}Immutable;
    }

    private ArrayList<String> _{{item_plural}} = new ArrayList<String>();
    private List<String> _{{item_plural}}Immutable = Collections.unmodifiableList(_{{item_plural}}s);
}
