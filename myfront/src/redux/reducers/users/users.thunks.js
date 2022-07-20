
import actions from './users.action';

export const loadUsersAsync = () => (dispatch) => {
    dispatch(actions.userLogin())
}

export const deleteUsersAsync = () => (dispatch) => {
    dispatch(actions.userLogout)
}

export const remembermeAsync = () => (dispatch) => {
    dispatch(actions.rememberMe)
}

