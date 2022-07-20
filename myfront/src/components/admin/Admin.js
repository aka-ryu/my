import react from "react";
import { Route, Router, Switch } from "react-router-dom";
import  axios  from 'axios';
import { useState } from "react";
import { useEffect } from "react";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import { Redirect } from "react-router-dom/cjs/react-router-dom";

const Admin = () => {

    const { userid } = useSelector(state => state.users);
    const history = useHistory();
    
    // if(!userid) {
    //     alert("관리자만 접근할수 있습니다.")
    //     return <Redirect push to ='/'/>
    // } else {
    //     axios.post(`/api/ck`)
    // }



        return (
            <div>
                <h1>어드민페이지</h1>
            </div>
        )
    

    
}

export default Admin;