import React from "react";
import { useCookies } from 'react-cookie';

function Home() {
    const [cookies, setCookie, removeCookie] = useCookies();
    return(
        <div>
            이거슨 홈이야
        </div>
    )
}

export default Home;