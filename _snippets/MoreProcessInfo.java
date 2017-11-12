{#- .default(package_path): org.mycompany -#}
package {{package_path}};

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

final class ProcessInfo
{
    public ProcessInfo(final String imageName, final int pid)
    {
        this.imageName = imageName;
        this.pid = pid;
    }

    public final String imageName;

    public final int pid;
}

final class ProcessInfoProvider implements AutoCloseable, Iterable<ProcessInfo>
{
    private CSVParser _parser;
    private Iterator<CSVRecord> _iterator;

    public static ProcessInfo[] getProcesses() throws IOException
    {
        try (final ProcessInfoProvider processInfoProvider = new ProcessInfoProvider())
        {
            ArrayList<ProcessInfo> processes = new ArrayList<ProcessInfo>();
            for (final ProcessInfo process : processInfoProvider)
            {
                processes.add(process);
            }

            return processes.toArray(new ProcessInfo[processes.size()]);
        }
    }

    public ProcessInfoProvider() throws IOException
    {
        final Process process = Runtime.getRuntime().exec(new String[]
            {
                Paths.get(System.getenv("SystemRoot"), "system32", "tasklist.exe").toString(),
                "/fo", "csv",
                "/nh"
            });
        _parser = CSVFormat.DEFAULT.parse(new InputStreamReader(process.getInputStream()));
        _iterator = _parser.iterator();
    }

    @Override
    public void close() throws IOException
    {
        _iterator = null;

        if (_parser != null)
        {
            _parser.close();
            _parser = null;
        }
    }

    @Override
    public Iterator<ProcessInfo> iterator()
    {
        return new Iterator<ProcessInfo>()
        {
            @Override
            public boolean hasNext()
            {
                return _iterator.hasNext();
            }

            @Override
            public ProcessInfo next()
            {
                final CSVRecord record = _iterator.next();
                if (record == null)
                {
                    return null;
                }

                final String imageName = record.get(0);
                final int pid = Integer.valueOf(record.get(1));
                return new ProcessInfo(imageName, pid);
            }
        };
    }
}

public final class Main
{
    public static void main(String[] args) throws IOException
    {
        for (final ProcessInfo process : ProcessInfoProvider.getProcesses())
        {
            System.out.println("imageName=" + process.imageName);
        }
    }
}
