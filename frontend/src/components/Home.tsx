import React, { Component } from 'react';
import Message from "semantic-ui-react/dist/commonjs/collections/Message/Message";

class Home extends Component {
    render() {
        return (
            <Message>
                <Message.Header>YAKB</Message.Header>
                <p>
                    Yet another kanban board application
                </p>
            </Message>
        );
    }
}

export default Home;
