{#- .name: Perform ad hoc logging to file -#}
{#- .default(output_path): C:\logging.txt -#}
#include <iostream>
#include <fstream>
#include <sstream>

namespace
{
    void RecordToFile(const std::string &what)
    {
        std::fstream myfile;
        myfile.open({{output_path | encode_cpp_literal }}, std::fstream::in | std::fstream::out | std::fstream::app);
        myfile << what << std::endl;
        myfile.close();
    }

    void RecordToFile(const std::stringstream &what)
    {
        std::fstream myfile;
        myfile.open({{output_path | encode_cpp_literal}}, std::fstream::in | std::fstream::out | std::fstream::app);
        myfile << what.str() << std::endl;
        myfile.close();
    }
}