import actionType from "./users.actionTypes";
import usersInitialStates from "./users.initialStates";

const usersReducer = (state = usersInitialStates, { type, payload }) => {

    switch (type) {

        case actionType.USER_LOGIN:
            return {
                ...state,
                userid: payload, 
            };

            case actionType.USER_LOGOUT:
            return {
                ...state,
                userid: null,
                rememberme: false,
            };

            case actionType.REMEMBER_ME:
                return {
                    ...state,
                    rememberme: true,
                }

        default:
            return state
    }
};

export default usersReducer;