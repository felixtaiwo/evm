import React, { useState, useEffect } from 'react';
import {
    Row,
    Card,
    CardTitle,
    Form,
    FormGroup,
    Label,
    Input,
    Button
} from 'reactstrap';
import { NavLink } from 'react-router-dom';
import { NotificationManager } from 'components/common/react-notifications';
import IntlMessages from 'helpers/IntlMessages';
import { Colxx } from 'components/common/CustomBootstrap';
import * as api from 'api'


const Register = () => {
    const [payload, setPayload] = useState({
        emailAddress: "",
        phone: "",
        firstname: "",
        lastname: "",
        middlename: "",
        identifier:""
    })
    const [loading, setLoading] = useState(false)
    const validateEmail = (email) => {
        if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)) {
            return (true)
        }

        return (false)
    };
    
    const onUserRegister = () => {
        if(validateEmail(payload.emailAddress)&&payload.firstname&&payload.lastname&&payload.phone){
            api.register(payload)
            .then(r=>{
                setLoading(false)
                NotificationManager.success("Account created successfully","Success",2000,null,null,null)
                location.replace(`/user/verify/${btoa(payload.emailAddress)}`)
            }) 
            .catch(e=>{
                setLoading(false)
                NotificationManager.error("Account created failed","Error",2000,null,null,null)
            })
        } else{
            NotificationManager.error("One or more fields have been incorrectly filled","Error",2000,null,null,null) 
        }

    };

    return (
        <Row className="h-100">
            <Colxx xxs="12" md="10" className="mx-auto my-auto">
                <Card className="auth-card">
                    <div className="position-relative image-side ">
                        <p className="text-white h2">MAKE YOUR CHOICE KNOWN</p>
                        <p className="white mb-0">
                            Please use this form to register. <br />
                            If you are a member, please{' '}
                            <NavLink to="/user/login" className="white">
                                <u><b>login</b></u>
                            </NavLink>
                            .
                        </p>
                    </div>
                    <div className="form-side">
                        <NavLink to="/" className="white">
                            <span className="logo-single" />
                        </NavLink>
                        <CardTitle className="mb-4">
                            <IntlMessages id="user.register" />
                        </CardTitle>
                        <Form>
                            <FormGroup className="form-group has-float-label  mb-4">
                                <Label>
                                    Firstname
                                </Label>
                                <Input type="name" defaultValue={payload.firstname} onChange={e => setPayload({ ...payload, firstname: e.target.value })} />
                            </FormGroup>
                            <FormGroup className="form-group has-float-label  mb-4">
                                <Label>
                                    Middlename
                                </Label>
                                <Input type="name" defaultValue={payload.middlename} onChange={e => setPayload({ ...payload, middlename: e.target.value })} />
                            </FormGroup>
                            <FormGroup className="form-group has-float-label  mb-4">
                                <Label>
                                    Lastname
                                </Label>
                                <Input type="name" defaultValue={payload.lastname} onChange={e => setPayload({ ...payload, lastname: e.target.value })} />
                            </FormGroup>
                            <FormGroup className="form-group has-float-label  mb-4">
                                <Label>
                                    Phone Number
                                </Label>
                                <Input type="name" defaultValue={payload.phone} onChange={e => setPayload({ ...payload, phone: e.target.value })} />
                            </FormGroup>
                            <FormGroup className="form-group has-float-label  mb-4">
                                <Label>
                                    Email Address
                                </Label>
                                <Input type="name" defaultValue={payload.emailAddress} onChange={e => setPayload({ ...payload, emailAddress: e.target.value })} />
                            </FormGroup>
                           {false&&<FormGroup className="form-group has-float-label  mb-4">
                                <Label>
                                    Unique Identifier
                                </Label>
                                <Input type="name" defaultValue={payload.identifier} onChange={e => setPayload({ ...payload, identifier: e.target.value })} />
                            </FormGroup>}

                            <div className="d-flex justify-content-end align-items-center">

                                <Button
                                    color="primary"
                                    className={`btn-shadow btn-multiple-state ${loading ? 'show-spinner' : ''
                                        }`}
                                    size="lg"
                                    onClick={onUserRegister}
                                >
                                    <span className="spinner d-inline-block">
                                        <span className="bounce1" />
                                        <span className="bounce2" />
                                        <span className="bounce3" />
                                    </span>
                                    <span className="label">
                                        Register
                                    </span>
                                </Button>
                            </div>
                        </Form>
                    </div>
                </Card>
            </Colxx>
        </Row>
    );
};

export default Register
