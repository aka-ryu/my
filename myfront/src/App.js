import { useEffect, useState, useReducer } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

//component
import Header from './components/layout/header';
import Home from './components/home';

import AuctionMain from './components/token/AuctionMain';
import Board from './components/board/Board';

//css
import './css/layout.css';
import './css/app.css';
import UserSignUp from './components/user/UserSignUp';
import UserSignIn from './components/user/UserSignIn';
import { lazy } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import actions from './redux/reducers/users/users.action';



import { Redirect } from 'react-router-dom/cjs/react-router-dom';
import Admin from './components/admin/Admin';

import MyPage from './components/myPage/MyPage';
import { useCookies } from 'react-cookie';



function App() {
  const dispatch = useDispatch();
  const { userid } = useSelector(state => state.users);
  const [loading, setLoading] = useState(true);
  const [cookies, setCookie, removeCookie] = useCookies();
  

  // axios.defaults.baseURL = "https://old-poems-wave-59-10-11-158.loca.lt/";
  axios.defaults.baseURL = "http://localhost:8080";
  axios.defaults.withCredentials = true;
  axios.defaults.headers.post['Content-Type'] = 'application/json';
  axios.defaults.headers.post['Accept'] = 'application/json';
  if (cookies.accessToken != null) {
    axios.defaults.headers.common['Authorization'] = "Bearer " + cookies.accessToken;
  }


  const check = async () => {
    if (cookies.logged && cookies.refreshToken && cookies.accessToken) {
          dispatch(actions.userLogin(cookies.logged));
    } else {
      removeCookie("remember")
      removeCookie("refreshToken")
      removeCookie("accessToken")
      removeCookie("logged")
    }


    setLoading(false)
  }
  
  useEffect(()=>{
    check()
  },[])



  if (loading) {
    return (
        <></>
    )
  } else {

    return (
      <div>

        <div className='container'>
          <Router>
            <Header />
            <Switch>
              <Redirect exact path='/' to='/board/list/' />
              <Route path="/board" component={Board}/>
              <Route path="/auction" component={AuctionMain} />
              <Route path="/user/sign-up">
                {userid ? <Redirect to='/' /> : <UserSignUp />}
              </Route>
              <Route path="/user/sign-in">
                {userid ? <Redirect to='/' /> : <UserSignIn />}
              </Route>
              <Route path="/admin" component={Admin} />
              <Route path="/mypage" component={MyPage} />
            </Switch>
          </Router>
        </div>
        {/* <Footer /> */}
      </div>
    );
  }
}

export default App;
