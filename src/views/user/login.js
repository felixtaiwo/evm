import React, { useState, useContext } from 'react';
import { Row, Card, CardTitle, Label, FormGroup, Button, CustomInput } from 'reactstrap';
import { NavLink, Redirect, useHistory } from 'react-router-dom';
import { Formik, Form, Field } from 'formik';
import { NotificationManager } from 'components/common/react-notifications';
import { Colxx } from 'components/common/CustomBootstrap';
import IntlMessages from 'helpers/IntlMessages';
import * as api from '../../api'
import { context } from '../../App'

// const validatePassword = (value) => {
//   let error;
//   if (!value) {
//     error = 'Please enter your password';
//   } else if (value.length < 8) {
//     error = 'Value must be longer than 8 characters';
//   }
//   return error;
// };


const Login = () => {
  const history = useHistory();
  const [payload, setPayload] = useState({
    username: "",
    password: "",
    role: "user"
  })
  // eslint-disable-next-line no-return-await,no-unused-vars
  const [error, setError] = useState(false)
  // eslint-disable-next-line no-return-await,no-unused-vars
  const [loading, setLoading] = useState(false)
  const [data, setData] = useContext(context)

  const onUserLogin = () => {
    if (!loading) {
      setLoading(true)
      api.login(payload)
        .then(r => {
            setLoading(false)
            NotificationManager.success("Login successful","Success",1000,null,null,null)
            setData({...data,auth:r.data.token,role:payload.role,user:r.data.user})
        })
        .catch(e => {
          setLoading(false)
          if (e.response.data.message === "default password in use") {
            location.replace(`/user/verify/${btoa(payload.username)}`)
          } else{
            if(e.response.data.message) NotificationManager.error(e.response.data.message.toUpperCase(),"ERROR",2000,null,null,null)
            else NotificationManager.error("Invalid Credentials","ERROR",2000,null,null,null)
          }
        })
    }
  };

  return (
    data.role ? <Redirect to="/app" /> : <Row className="h-100">
      <Colxx xxs="12" md="10" className="mx-auto my-auto">
        <Card className="auth-card">
          <div className="position-relative image-side ">

          </div>
          <div className="form-side">

            <CardTitle className="mb-4">
              <b>LOGIN</b>
            </CardTitle>

            <Formik>
              {({ errors, touched }) => (
                <Form className="av-tooltip tooltip-label-bottom">
                  <FormGroup className="form-group has-float-label">
                    <Label>
                      Email Address or Phone Number
                    </Label>
                    <Field
                      className="form-control"
                      name="email"
                      value={payload.username}
                      onChange={e => setPayload({ ...payload, username: e.target.value })}
                    />
                    {errors.email && touched.email && (
                      <div className="invalid-feedback d-block">
                        {errors.email}
                      </div>
                    )}
                  </FormGroup>
                  <FormGroup className="form-group has-float-label">
                    <Label>
                      Password
                    </Label>
                    <Field
                      className="form-control"
                      type="password"
                      name="password"
                      value={payload.password}
                      onChange={e => setPayload({ ...payload, password: e.target.value })}
                    />
                    {errors.password && touched.password && (
                      <div className="invalid-feedback d-block">
                        {errors.password}
                      </div>
                    )}
                  </FormGroup>
                  <CustomInput
                    type="radio"
                    name="radiusRadio"
                    id="user"
                    label="User"
                    inline
                    defaultChecked={payload.role === 'user'}
                    onChange={() => setPayload({ ...payload, role: "user" })}
                  />
                  <CustomInput
                    type="radio"
                    name="radiusRadio"
                    id="admin"
                    label="Admin"
                    inline
                    defaultChecked={payload.role === 'admin'}
                    onChange={() => setPayload({ ...payload, role: "admin" })}
                  />

                  <div className="d-flex justify-content-between align-items-center">
                    <NavLink to="/user/forgot-password">
                      <IntlMessages id="user.forgot-password-question" />
                    </NavLink><br />
                    <p className=" mb-0">
                      <NavLink to="/user/register" className="">
                        <u><b>Register Here</b></u>
                      </NavLink>

                    </p>
                    <Button
                      color="primary"
                      className={`btn-shadow btn-multiple-state ${loading ? 'show-spinner' : ''
                        }`}
                      size="lg"
                      onClick={onUserLogin}
                    >
                      <span className="spinner d-inline-block">
                        <span className="bounce1" />
                        <span className="bounce2" />
                        <span className="bounce3" />
                      </span>
                      <span className="label">
                        <IntlMessages id="user.login-button" />
                      </span>
                    </Button>

                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </Card>
      </Colxx>
    </Row>
  );
};
export default Login
