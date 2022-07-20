import React from "react";
import { BrowserRouterasRouter, Route, Switch, Redirect, BrowserRouter, useHistory } from 'react-router-dom';


//component
import BoardDetail from "./BoardDetail";
import BoardList from './BoardList';
import BoardWrite from "./BoardWrite";
import BoardModify from './BoardModify';
import { useEffect } from "react";
import { useSelector } from 'react-redux';


function Board() {
    const { userid } = useSelector(state => state.users);


    return (
        <BrowserRouter>
            <Switch>
                {/* <Redirect exact from="/board" to="/board/list?page=1"/> */}
                <Route  path={`/board/list/`} component={BoardList}></Route>
                <Route  path={`/board/detail/`}>
                    {userid ? <BoardDetail/> : <Redirect to='/board/list/' />}
                </Route>
                <Route  path={`/board/edit/`} >
                    {userid ? <BoardModify/> : <Redirect to='/board/list/' />}
                </Route>
                <Route  path={`/board/write/`} >
                    {userid ? <BoardWrite/> : <Redirect to='/board/list/' />}
                </Route>
            
            </Switch>
        </BrowserRouter>
        // <Redirect from="/board" to="/board/list/1"/>
    );
}

export default Board;