import React, { Suspense } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';

const Start = React.lazy(() =>
  import(/* webpackChunkName: "start" */ './start')
);
const Result = React.lazy(() =>
  import(/* webpackChunkName: "start" */ './ResultTable')
);
const Gogo = ({ match }) => (
  <Suspense fallback={<div className="loading" />}>
    <Switch>
      <Redirect exact from={`${match.url}/`} to={`${match.url}/dashboard`} />
      <Route
        path={`${match.url}/dashboard`}
        render={(props) => <Start {...props} />}
      />
      <Route
        path={`${match.url}/results`}
        render={(props) => <Result {...props} />}
      />
      <Redirect to="/error" />
    </Switch>
  </Suspense>
);
export default Gogo;
