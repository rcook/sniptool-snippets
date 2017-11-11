{#- .name: Java class implementing some process-control functionality -#}
{#- .description: Kill process by listening port etc. -#}
{#- .default(class_name): ProcessUtil -#}
{#- .default(package_path): org.mycompany -#}
package {{package_path}};

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.jna.Platform;

final class ProcessInfo
{
    public ProcessInfo(int pid, int listeningPort)
    {
        this.pid = pid;
        this.listeningPort = listeningPort;
    }

    public final int pid;

    public final int listeningPort;
}

public final class {{class_name}}
{
    private {{class_name}}()
    {
    }

    public static void killProcessByListeningPort(final int listeningPort)
    {
        Integer pid = getProcessByListeningPort(listeningPort);
        if (pid != null)
        {
            killProcess(pid);
        }
    }

    private static Integer getProcessByListeningPort(final int listeningPort)
    {
        final String[] command = makeNetstatCommand();

        try
        {
            final Process p = Runtime.getRuntime().exec(command);
            try (final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())))
            {
                String line;
                while ((line = input.readLine()) != null)
                {
                    ProcessInfo processInfo = parseNetstatProcessInfo(line);
                    if (processInfo != null && processInfo.listeningPort == listeningPort)
                    {
                        return processInfo.pid;
                    }
                }
            }
        }
        catch (IOException e)
        {
            return null;
        }

        return null;
    }

    private static String[] makeNetstatCommand()
    {
        return Platform.isWindows() ? makeNetstatCommandWindows() : makeNetstatCommandLinux();
    }

    private static String[] makeNetstatCommandWindows()
    {
        return new String[]
        {
            String.format("%s\\system32\\netstat.exe", System.getenv("SystemRoot")),
            "-a",   // Displays all connections and listening ports
            "-n",   // Displays addresses and port numbers in numerical form
            "-o"    // Displays the owning process ID associated with each connection
        };
    }

    private static String[] makeNetstatCommandLinux()
    {
        return new String[]
        {
            "netstat",
            "-tulpn"
        };
    }

    private static ProcessInfo parseNetstatProcessInfo(final String s)
    {
        return Platform.isWindows() ? parseNetstatProcessInfoWindows(s) : parseNetstatProcessInfoLinux(s);
    }

    private static ProcessInfo parseNetstatProcessInfoWindows(final String s)
    {
        final String[] columns = s.split("\\s+");
        if (columns.length != 6)
        {
            return null;
        }

        final String protocol = columns[1].toLowerCase();
        if (!protocol.equals("tcp"))
        {
            return null;
        }

        final String status = columns[4].toLowerCase();
        if (!status.equals("listening"))
        {
            return null;
        }

        final Integer pid = parseInteger(columns[5]);
        if (pid == null)
        {
            return null;
        }

        final Integer listeningPort = parsePortColumn(columns[2]);
        if (listeningPort == null)
        {
            return null;
        }

        return new ProcessInfo(pid, listeningPort);
    }

    private static ProcessInfo parseNetstatProcessInfoLinux(final String s)
    {
        final String[] columns = s.split("\\s+");
        if (columns.length != 7)
        {
            return null;
        }

        final String protocol = columns[0].toLowerCase();
        if (!protocol.equals("tcp") && !protocol.equals("tcp6"))
        {
            return null;
        }

        final String status = columns[5].toLowerCase();
        if (!status.equals("listen"))
        {
            return null;
        }

        final Integer pid = parsePidColumn(columns[6]);
        if (pid == null)
        {
            return null;
        }

        final Integer listeningPort = parsePortColumn(columns[3]);
        if (listeningPort == null)
        {
            return null;
        }

        return new ProcessInfo(pid, listeningPort);
    }

    private static void killProcess(int pid)
    {
        try
        {
            Runtime.getRuntime().exec(makeKillCommand(pid));
        }
        catch (IOException e)
        {
        }
    }

    private static String[] makeKillCommand(int pid)
    {
        return Platform.isWindows() ? makeKillCommandWindows(pid) : makeKillCommandLinux(pid);
    }

    private static String[] makeKillCommandWindows(int pid)
    {
        return new String[]
        {
            String.format("%s\\system32\\taskkill.exe", System.getenv("SystemRoot")),
            "/F",   // Specifies to forcefully terminate the process(es)
            "/T",   // Terminates the specified process
            "/PID", String.valueOf(pid),    // Specifies the PID of the process to be terminated
        };
    }

    private static String[] makeKillCommandLinux(int pid)
    {
        return new String[]
        {
            "kill",
            "-9",   // SIGKILL
            String.valueOf(pid),
        };
    }

    private static Integer parsePortColumn(final String s)
    {
        final int index = s.lastIndexOf(':');
        if (index == -1)
        {
            return null;
        }

        final String raw = s.substring(index + 1);
        return parseInteger(raw);
    }

    private static Integer parsePidColumn(final String s)
    {
        final int index = s.indexOf('/');
        if (index == -1)
        {
            return null;
        }

        final String raw = s.substring(0, index);
        return parseInteger(raw);
    }

    private static Integer parseInteger(final String s)
    {
        try
        {
            return Integer.valueOf(s);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
}
