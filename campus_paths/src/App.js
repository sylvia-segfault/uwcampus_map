/*
 * Copyright (C) 2020 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import Map from "./Map";
import Dropdownlist from "./Dropdownlist";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // store a map that maps from building's short names to their long names
            listBuilding: [],
            startBuilding: "PAR", // user's query of the starting point
            endBuilding: "PAR", // user's query of the destination point
            pathObject: "" // store the shortest path between two places
        };
    }

    async componentDidMount() {
        // once the component is created, we want to show the options of all the
        // buildings on campus
        this.getListBuilding();
    }

    getListBuilding = async () => {
        try {
            // fetch the information of a list of buildings from Spark Server
            let response = await fetch("http://localhost:4567/list-building");
            // If the response doesn't have the right status code that indicates that
            // we received a valid response, alert the user.
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            // get the JSON String of the list of buildings
            let listOfBuilding = await response.json();
            // update the state that stores a map which maps from building's short names
            // to their long names
            this.setState({
                listBuilding: listOfBuilding
            });
        } catch (e) {
            // alert the user if there is an error happening
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    updateStartPoint = (start) => {
        // update the starting point queried by the user
        this.setState({
            startBuilding: start
        });
    };

    updateEndPoint = (end) => {
        // update the destination point queried by the user
        this.setState({
            endBuilding: end
        });
    };

    getPath = async () => {
        try {
            // We need to get short names of the buildings because findShortestPath we wrote
            // before can only recognize short names of them.
            // Get the short name of the starting point
            let shortNameStart = this.state.startBuilding.split(":")[0];
            // Get the short name of the destination point
            let shortNameEnd = this.state.endBuilding.split(":")[0];
            // Alert the user if he tries to find a path from a building to itself
            if (shortNameStart === shortNameEnd) {
                alert("We can't find a path from one building to itself!")
            } else {
                // Fetch the information of the shortest path between these two places
                let response = await fetch("http://localhost:4567/find-path?start=" + shortNameStart + "&end="
                    + shortNameEnd);
                // If the response doesn't have the right status code that indicates that
                // we received a valid response, alert the user.
                if (!response.ok) {
                    alert("The status is wrong! Expected: 200, Was: " + response.status);
                    return; // Don't keep trying to execute if the response is bad.
                }
                // get the JSON String of the shortest path between the two places queried by
                // the user
                let requestedPath = await response.json();
                // update the shortest path we found.
                // JSON string contains Path<Point> and Path has a field named path.
                // Therefore, We use requestedPath.path because it gives us information
                // of a list of segments that represents the shortest path between the two
                // places queried by the user
                this.setState({
                    pathObject: requestedPath.path
                });
            }
        } catch (e) {
            // alert the user if there is an error happening
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    resetButton = async () => {
        // reset everything back to their default values
        // when the user tries to reset everything
        this.setState({
            listBuilding: [],
            startBuilding: "PAR",
            endBuilding: "PAR",
            pathObject: ""
        });
        // Notice here we still need to call this function getListBuilding()
        // because we still want to present the options to the user in case
        // if he wants to find a path between two other buildings.
        // Furthermore, the options should always be presented to the user
        // and reset shouldn't delete those options.
        this.getListBuilding();
    };

    render() {
        // the name "onchange" for these two dropdownlist components has to be the same
        // because dropdownlist component is reusing "onChange".
        // This is fine because the user will input the starting point first and then
        // input the destination point. Setting "onChange" to two different update
        // functions (this.updateStartPoint/this.updateEndPoint) takes care of
        // everything and React would know which change is to which state.
        return (
            <div>
                <p id="app-title">Husky Map</p>
                <p1>Choose your starting point:</p1>
                <Dropdownlist listBuilding={this.state.listBuilding}
                              onChange={this.updateStartPoint}/>
                <br/>
                <p2>Choose your destination point:</p2>
                <Dropdownlist listBuilding={this.state.listBuilding}
                              onChange={this.updateEndPoint}/>
                <br/>
                <button style={{marginRight: 21 + 'em'}} onClick={this.getPath}>Find Path</button>
                <button onClick={this.resetButton}>Reset</button>
                <Map pathObject={this.state.pathObject}/>
            </div>
        );
    };
}

export default App;