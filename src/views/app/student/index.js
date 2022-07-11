import React, { Suspense } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';

const Second = React.lazy(() =>
  import(/* webpackChunkName: "second" */ './second')
);
const Second2 = React.lazy(() =>
  import(/* webpackChunkName: "second" */ './second2')
);
const Second3 = React.lazy(() =>
  import(/* webpackChunkName: "second" */ './second3')
);
const SecondMenu = ({ match }) => (
  <Suspense fallback={<div className="loading" />}>
    <Switch>
      <Redirect exact from={`${match.url}/`} to={`${match.url}/dashboard`} />
      <Route
        path={`${match.url}/dashboard`}
        render={(props) => <Second {...props} />}
      />
      <Route
        path={`${match.url}/polls`}
        render={(props) => <Second2 {...props} />}
      />
       <Route
        path={`${match.url}/account`}
        render={(props) => <Second3 {...props} />}
      />
      <Redirect to="/error" />
    </Switch>
  </Suspense>
);
export default SecondMenu;
