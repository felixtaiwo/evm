import React, { useContext, useEffect, useState } from 'react';
import { Colxx, Separator } from 'components/common/CustomBootstrap';
import Breadcrumb from 'containers/navs/Breadcrumb';
import { context } from 'App';
import { Form, Formik, Field } from 'formik';
import { Row, CardTitle, Label, FormGroup, Button, CustomInput } from 'reactstrap';
import * as api from '../../../api'
import { NotificationManager } from 'components/common/react-notifications';

const Second3 = ({ match }) => {
    const [data, setData] = useContext(context)
    const [loading, setLoading] = useState(false)
    const [userData, setUserData] = useState(data.user)
    const [edit, setEdit] = useState(false)
    const [changePassword,setChangePassword]=useState(false)
    const [passwordPayload,setPasswordPayload]=useState({
        email:data.user.emailAddress,
        oldpassword:"",
        newpassword:"",
        cc:""
    })
    const handleLogout = () => {
        sessionStorage.clear();
        location.replace('/user/login')
        NotificationManager.warning("Logout successful", "Success", 2500, null, null, '')
    };
    const onUserEdit = () => {
        setLoading(true)
        if(changePassword){
            api.changePassword(passwordPayload)
            .then(r=>{
                setLoading(false)
                NotificationManager.success("Password Change Successful", "Successful", 1500, null, null, null)
                setPasswordPayload({...passwordPayload,oldpassword:"",newpassword:"",cc:""})
                setChangePassword(false)
            })
            .catch(e=>{
                setLoading(false)
                NotificationManager.error("Password Change failed", "Error", 1500, null, null, null)
            })
            return
        }
        if (!edit) {
            setEdit(true)
            return
        }
        api.register(userData)
            .then(r => {
                setLoading(false)
                NotificationManager.success("Account Updation Successful", "Successful", 1500, null, null, null)
                if (userData.phone !== data.user.phone || userData.emailAddress !== data.user.emailAddress) {
                    NotificationManager.info("Logging out, please reverify account", "Verification Needed", 3000, null, null, null)
                    handleLogout()
                    return
                }
                setData({ ...data, user: userData })
                location.replace("/")
            })
            .catch(e => {
                setLoading(false)
                NotificationManager.error("Account Updation Failed", "Error", 2000, null, null, null)
            })
    }
    return (
        <>
            <Row>
                <Colxx xxs="12">
                    <Breadcrumb heading="menu.second3" match={match} />
                    <Separator className="mb-5" />
                </Colxx>
            </Row>
            <Row>
                <Colxx xxs="12" className="mb-4">
                    <div className="form-side">

                        <CardTitle className="mb-4">
                            Profile
                        </CardTitle>
                        <Formik>
                            {() => (
                                <Form className="av-tooltip tooltip-label-bottom">
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Firstname
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(userData.firstname)}
                                            disabled={!edit}
                                            onChange={e => setUserData({ ...userData, firstname: e.target.value })}
                                        />

                                    </FormGroup>
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Middlename
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(userData.middlename)}
                                            disabled={!edit}
                                            onChange={e => setUserData({ ...userData, middlename: e.target.value })}
                                        />

                                    </FormGroup>
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Lastname
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(userData.lastname)}
                                            disabled={!edit}
                                            onChange={e => setUserData({ ...userData, lastname: e.target.value })}
                                        />

                                    </FormGroup>
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Email Address
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(userData.emailAddress)}
                                            disabled={!edit}
                                            onChange={e => setUserData({ ...userData, emailAddress: e.target.value })}
                                        />

                                    </FormGroup>
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Phone Number
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(userData.phone)}
                                            disabled={!edit}
                                            onChange={e => setUserData({ ...userData, phone: e.target.value })}
                                        />

                                    </FormGroup>
                                    {changePassword&&<>
                                    
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Password
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(passwordPayload.oldpassword)}
                                            disabled={!changePassword}
                                            onChange={e => setPasswordPayload({ ...passwordPayload, oldpassword: e.target.value })}
                                            type="password"
                                        />

                                    </FormGroup>
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            New Password
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(passwordPayload.newpassword)}
                                            disabled={!changePassword}
                                            onChange={e => setPasswordPayload({ ...passwordPayload, newpassword: e.target.value })}
                                            type="password"
                                        />

                                    </FormGroup>
                                    <FormGroup className="form-group has-float-label sm">
                                        <Label>
                                            Confirm New Password
                                        </Label>
                                        <Field
                                            className="form-control"
                                            name="email"
                                            value={(passwordPayload.cc)}
                                            disabled={!changePassword}
                                            onChange={e => setPasswordPayload({ ...passwordPayload, cc: e.target.value })}
                                            type="password"
                                        />

                                    </FormGroup></>}
                                    <CustomInput
                                        className="itemCheck mb-0 mx-2"
                                        type="checkbox"
                                        checked={changePassword}
                                        onChange={(event) => {setChangePassword(!changePassword);setEdit(false);!changePassword&&NotificationManager.info("Password must contain at least 8 characters","Info",5000)}}
                                        label="Change Password"
                                    />


                                    <div className="d-flex justify-content-between align-items-center">

                                        {<Button
                                            color="primary"
                                            className={`btn-shadow btn-multiple-state ${loading ? 'show-spinner' : ''
                                                }`}
                                            size="lg"
                                            onClick={onUserEdit}
                                            disabled={changePassword?!(passwordPayload.oldpassword.length>=8&&passwordPayload.newpassword.length>=8&&passwordPayload.newpassword===passwordPayload.cc):false}

                                        >
                                            <span className="spinner d-inline-block">
                                                <span className="bounce1" />
                                                <span className="bounce2" />
                                                <span className="bounce3" />
                                            </span>
                                            <span className="label">
                                                {changePassword?"Change Password":edit ? "Submit" : "Edit Profile"}
                                            </span>
                                        </Button>}
                                    </div>
                                </Form>
                            )}
                        </Formik>
                    </div>
                </Colxx>
            </Row>


        </>)
};
export default Second3;
