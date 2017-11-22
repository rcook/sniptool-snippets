{#- .name: Java custom exception and error code -#}
{#- .description: Follows best practices, I hope -#}
{#- .default(package_path): org.mycompany -#}
{#- .default(error_code_class_name): MyErrorCode -#}
{#- .default(exception_class_name): MyException -#}
////////// BEGIN {{error_code_class_name}}.java //////////
package {{package_path}};

/**
 * This is a custom error code
 *
 * @author user
 */
public enum {{error_code_class_name}}
{
    INVALID_CONDITION(1, "Invalid condition");

    private final int _id;
    private final String _description;

    {{error_code_class_name}}(final int id, final String description)
    {
        _id = id;
        _description = description;
    }

    public int getId()
    {
        return _id;
    }

    public String getDescription()
    {
        return _description;
    }

    @Override
    public String toString()
    {
        return _id + ": " + _description;
    }
}
////////// BEGIN {{exception_class_name}}.java //////////
package {{package_path}};

/**
 * This is a custom exception
 *
 * @author user
 */
public final class {{exception_class_name}} extends Exception
{
    //private static final long serialVersionUID = /* some serialization ID */;

    private final {{error_code_class_name}} _errorCode;

    public {{exception_class_name}}(final String message, final Throwable cause, final {{error_code_class_name}} errorCode)
    {
        super(message, cause);
        _errorCode = errorCode;
    }

    public {{exception_class_name}}(final String message, final {{error_code_class_name}} errorCode)
    {
        super(message);
        _errorCode = errorCode;
    }

    public {{exception_class_name}}(final Throwable cause, final {{error_code_class_name}} errorCode)
    {
        super(cause);
        _errorCode = errorCode;
    }

    public {{exception_class_name}}(final {{error_code_class_name}} errorCode)
    {
        super();
        _errorCode = errorCode;
    }

    public {{error_code_class_name}} getErrorCode()
    {
        return _errorCode;
    }
}
