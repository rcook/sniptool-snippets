{#- .default(package_path): org.mycompany -#}
package {{package_path}};

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

final class LineIterable implements AutoCloseable, Iterable<String>
{
    private BufferedReader _reader;

    public LineIterable(BufferedReader reader)
    {
        if (reader == null)
        {
            throw new IllegalArgumentException();
        }

        _reader = reader;
    }

    @Override
    public void close() throws IOException
    {
        if (_reader != null)
        {
            _reader.close();
            _reader = null;
        }
    }

    @Override
    public Iterator<String> iterator()
    {
        return new Iterator<String>()
        {
            private String _line = readLineHelper();

            @Override
            public boolean hasNext()
            {
                return _line != null;
            }

            @Override
            public String next()
            {
                if (_line == null)
                {
                    return null;
                }

                final String temp = _line;
                _line = readLineHelper();
                return temp;
            }
        };
    }

    private String readLineHelper()
    {
        try
        {
            return _reader.readLine();
        }
        catch (IOException e)
        {
            return null;
        }
    }
}

public final class Main
{
    public static void main(String[] args) throws IOException
    {
        try (final LineIterable lines = new LineIterable(new BufferedReader(new StringReader("aaa\nbbb\nccc\nddd"))))
        {
            for (final String line : lines)
            {
                System.out.println(line);
            }
        }
    }
}
