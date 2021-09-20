import React, {Component} from 'react';
import CommandService from '../services/CommandService';

class CommandComponent extends Component
{
    constructor(props) {
        super(props)
        
        this.state = {
            input: null,
            message: null
        }
        this.sendCommand = this.sendCommand.bind(this)
    }

    sendCommand(command) {
        CommandService.parseCommand(command)
            .then(
                response => {
                    console.log(response)
                    this.setState({ message: response.data })
                }
            )
    }

    handleEnter = (event) => {
        if(event.key === 'Enter')
        {
            this.sendCommand(event.target.value)
            this.setState({input: ''})
        }
      }

    handleChange(event)
    {
        this.setState({input: event.target.value})
    }

    render() {
        return (
            <div className="container">
                <h1>Welcome</h1>
                
                <div className="container">
                    <table className="table" style={{textAlign:"left"}}>
                        <thead>
                            <tr>
                                <th>Command</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>login `client`</td>
                                <td>Login as `client`. Creates a new client if not yet exists.</td>
                            </tr>
                            <tr>
                                <td>topup `amount`</td>
                                <td>Increase logged-in client balance by `amount`.</td>
                            </tr>
                            <tr>
                                <td>pay `another_client` `amount`</td>
                                <td>Pay `amount` from logged-in client to `another_client`, maybe in parts, as soon as possible.</td>
                            </tr>
                        </tbody>
                    </table>

                    {this.state.message && <div class="alert alert-success">{this.state.message}</div>}

                    <input type="text" id="commandBox" label="Standard" size="50" placeholder="Enter command here" value={this.state.input} onKeyPress={this.handleEnter} onChange={this.handleChange.bind(this)}/>
                </div>
            </div>
        )
    }
}

export default CommandComponent;