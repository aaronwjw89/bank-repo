import axios from 'axios';

//const PARSE_COMMAND_API_URL = 'http://localhost:8080/v1/home';

class CommandService
{
    parseCommand(command)
    {
        return axios.get(`http://localhost:8080/v1/home/${command}`);
    }
}

export default new CommandService();