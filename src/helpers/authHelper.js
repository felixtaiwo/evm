import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { isAuthGuardActive } from 'constants/defaultValues';

const ProtectedRoute = ({
  component: Component,
  roles = undefined,
  ...rest
}) => {
  const setComponent = (props) => {
    if (isAuthGuardActive) {
        if (roles!=={}) {
            return roles.role!==""?<Component {...props} />:<Redirect
            to={{
              pathname: '/user/login',
              state: { from: props.location },
            }}
          />;
        }
          
        return <Component {...props} />;
      
    }
    return <Component {...props} />;
  };

  return <Route {...rest} render={setComponent} />;
};

// eslint-disable-next-line import/prefer-default-export
export { ProtectedRoute };
