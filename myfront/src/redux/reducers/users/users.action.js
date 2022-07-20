import usersAction from "./users.actionTypes";

const userLogin = (user) => ({
    type: usersAction.USER_LOGIN,
    payload: user,
})

const userLogout = () => ({
    type: usersAction.USER_LOGOUT
})

const rememberMe = () => ({
    type: usersAction.REMEMBER_ME
})

export default {
    userLogin,
    userLogout,
    rememberMe
}