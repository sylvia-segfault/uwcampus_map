import React, {Component} from 'react';

class Dropdownlist extends Component {
    constructor(props) {
        super(props);
        this.state = {
            takeInput: "" // stores the input of the user
        };
    }

    handleChange = (event) => {
        // get the input of the user
        let input = event.target.value;
        // the callback function which updates the corresponding states in the
        // parent (App) component
        this.props.onChange(input);
        // update the state so that it stores the user's input
        this.setState({
            takeInput: input
        });
    };

    render() {
        // get the list of buildings from the parent component
        let listOfBuildings = this.props.listBuilding;
        let list = []; // a list that stores available options (buildings) on campus
        // loop through the short names of the buildings
        for (let shortName of Object.keys(listOfBuildings)) {
            // push the each option onto the list
            // each option has the building's short name followed by ":" and then followed by
            // its corresponding long name.
            list.push(<option key={shortName}>{shortName + ": " + listOfBuildings[shortName]}</option>);
        }
        return (
            <div>
                <select value={this.state.takeInput} onChange={this.handleChange}>
                    {list}
                </select>
            </div>
        );
    };
}

export default Dropdownlist;