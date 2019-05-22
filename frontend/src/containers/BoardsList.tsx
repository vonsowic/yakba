import React, {Component} from 'react';
import Message from "semantic-ui-react/dist/commonjs/collections/Message/Message";
import List from "semantic-ui-react/dist/commonjs/elements/List/List";

class BoardsList extends Component {
    render() {
        return (
            <List divided relaxed>
                <List.Item>
                    <List.Icon name='github' size='large' verticalAlign='middle' />
                    <List.Content>
                        <List.Header as='a'>Semantic-Org/Semantic-UI</List.Header>
                        <List.Description as='a'>Updated 10 mins ago</List.Description>
                    </List.Content>
                </List.Item>
            </List>
        );
    }
}

export default BoardsList;
