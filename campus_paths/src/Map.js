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
import "./Map.css";

class Map extends Component {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    constructor(props) {
        super(props);
        this.state = {
            backgroundImage: null
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        // When our component first mounts, we want to load the image
        // and put it on the canvas of the web page
        this.fetchAndSaveImage();
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        // When the state gets updated, we want to draw the background image
        // because backgroudImage is set to a resource image and becomes non-null
        this.drawBackgroundImage();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    drawBackgroundImage() {
        let canvas = this.canvas.current;
        let ctx = canvas.getContext("2d");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
        // Get the list of segments that represents the shortest path we
        // are going to draw
        let listOfSegments = this.props.pathObject;
        // loop through the list of segments
        for (let index of Object.keys(listOfSegments)) {
            // get the current segment
            let segment = listOfSegments[index];
            // define this object lineOfPath so that it has correct
            // starting coordinate (x1, y1) and correct ending
            // coordinate (x2, y2)
            let lineOfPath = {
                x1: segment.start.x,
                y1: segment.start.y,
                x2: segment.end.x,
                y2: segment.end.y
            };
            ctx.beginPath(); // begin a path
            ctx.strokeStyle = "HotPink"; // specify the color of this line
            ctx.lineWidth = 12; // specify the line width
            // define a point (x1, y1)
            ctx.moveTo(lineOfPath.x1, lineOfPath.y1);
            // define a line from the last specified point(x1, y1) to (x2, y2)
            ctx.lineTo(lineOfPath.x2, lineOfPath.y2);
            ctx.stroke(); // draw the defined line
            ctx.closePath(); // close the path
        }
    }

    render() {
        return (
            <div id="map canvas">
                <canvas ref={this.canvas}/>
            </div>

        )
    }
}

export default Map;