import React from 'react';
import { Row } from 'reactstrap';
import { Colxx, Separator } from 'components/common/CustomBootstrap';
import Breadcrumb from 'containers/navs/Breadcrumb';
import DatePickerExamples from './DatePickerExamples';

const Start = ({ match }) => (
  <>
    <Row>
      <Colxx xxs="12">
        <Breadcrumb heading="menu.start" match={match} />
        <Separator className="mb-5" />
      </Colxx>
    </Row>
    <Row>
      <Colxx xxs="12" className="mb-4">
        <h2>
         <b>Administrative Console</b>
        </h2>
      </Colxx>
    </Row>
    <Row>
      <Colxx xxs="12" className="mb-4">
       <DatePickerExamples/>
      </Colxx>
    </Row>
  </>
);
export default Start;
