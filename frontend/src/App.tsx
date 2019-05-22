import React, {Component} from 'react';
import {BrowserRouter, Link} from 'react-router-dom';
import Home from "./components/Home";
import {Route, Switch} from "react-router";
import BoardsList from "./containers/BoardsList";
import Board from "./containers/Board";
import Login from "./containers/Login";
import Container from "semantic-ui-react/dist/commonjs/elements/Container/Container";
import Menu from "semantic-ui-react/dist/commonjs/collections/Menu/Menu";

class App extends Component {
    render() {
        return (
            <div>
                <BrowserRouter>
                    <Menu>
                        <Menu.Item as={Link} to='/'>Home</Menu.Item>
                        <Menu.Item as={Link} to='/login'>Login</Menu.Item>
                        <Menu.Item as={Link} to='/boards'>Boards</Menu.Item>
                    </Menu>
                    <Container>
                        <Switch>
                            <Route exact path='/' component={Home}/>
                            <Route exact path='/login' component={Login}/>
                            <Route exact path='/boards' component={BoardsList}/>
                            <Route path='/boards/:boardId' component={Board}/>
                        </Switch>
                    </Container>
                </BrowserRouter>
            </div>
        );
    }
}

export default App;
