import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './events.reducer';

export const EventsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventsEntity = useAppSelector(state => state.events.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventsDetailsHeading">
          <Translate contentKey="rfidh2ElkApp.events.detail.title">Events</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.id}</dd>
          <dt>
            <span id="guid">
              <Translate contentKey="rfidh2ElkApp.events.guid">Guid</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.guid}</dd>
          <dt>
            <span id="plate">
              <Translate contentKey="rfidh2ElkApp.events.plate">Plate</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.plate}</dd>
          <dt>
            <span id="anpr">
              <Translate contentKey="rfidh2ElkApp.events.anpr">Anpr</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.anpr}</dd>
          <dt>
            <span id="rfid">
              <Translate contentKey="rfidh2ElkApp.events.rfid">Rfid</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.rfid}</dd>
          <dt>
            <span id="gantry">
              <Translate contentKey="rfidh2ElkApp.events.gantry">Gantry</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.gantry}</dd>
          <dt>
            <span id="wantedFor">
              <Translate contentKey="rfidh2ElkApp.events.wantedFor">Wanted For</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.wantedFor}</dd>
          <dt>
            <span id="licenseIssue">
              <Translate contentKey="rfidh2ElkApp.events.licenseIssue">License Issue</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.licenseIssue}</dd>
          <dt>
            <span id="issue">
              <Translate contentKey="rfidh2ElkApp.events.issue">Issue</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.issue}</dd>
          <dt>
            <span id="tagIssue">
              <Translate contentKey="rfidh2ElkApp.events.tagIssue">Tag Issue</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.tagIssue}</dd>
          <dt>
            <span id="statusName">
              <Translate contentKey="rfidh2ElkApp.events.statusName">Status Name</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.statusName}</dd>
          <dt>
            <span id="lane">
              <Translate contentKey="rfidh2ElkApp.events.lane">Lane</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.lane}</dd>
          <dt>
            <span id="direction">
              <Translate contentKey="rfidh2ElkApp.events.direction">Direction</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.direction}</dd>
          <dt>
            <span id="kph">
              <Translate contentKey="rfidh2ElkApp.events.kph">Kph</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.kph}</dd>
          <dt>
            <span id="ambush">
              <Translate contentKey="rfidh2ElkApp.events.ambush">Ambush</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.ambush}</dd>
          <dt>
            <span id="toll">
              <Translate contentKey="rfidh2ElkApp.events.toll">Toll</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.toll}</dd>
          <dt>
            <span id="fine">
              <Translate contentKey="rfidh2ElkApp.events.fine">Fine</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.fine}</dd>
          <dt>
            <span id="wantedBy">
              <Translate contentKey="rfidh2ElkApp.events.wantedBy">Wanted By</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.wantedBy}</dd>
          <dt>
            <span id="licenseFine">
              <Translate contentKey="rfidh2ElkApp.events.licenseFine">License Fine</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.licenseFine}</dd>
          <dt>
            <span id="speedFine">
              <Translate contentKey="rfidh2ElkApp.events.speedFine">Speed Fine</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.speedFine}</dd>
          <dt>
            <span id="handled">
              <Translate contentKey="rfidh2ElkApp.events.handled">Handled</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.handled}</dd>
          <dt>
            <span id="processingTime">
              <Translate contentKey="rfidh2ElkApp.events.processingTime">Processing Time</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.processingTime}</dd>
          <dt>
            <span id="ruleRcvd">
              <Translate contentKey="rfidh2ElkApp.events.ruleRcvd">Rule Rcvd</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.ruleRcvd}</dd>
          <dt>
            <span id="ruleIssue">
              <Translate contentKey="rfidh2ElkApp.events.ruleIssue">Rule Issue</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.ruleIssue}</dd>
          <dt>
            <span id="ruleProcessed">
              <Translate contentKey="rfidh2ElkApp.events.ruleProcessed">Rule Processed</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.ruleProcessed}</dd>
          <dt>
            <span id="ruleSent">
              <Translate contentKey="rfidh2ElkApp.events.ruleSent">Rule Sent</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.ruleSent}</dd>
          <dt>
            <span id="when">
              <Translate contentKey="rfidh2ElkApp.events.when">When</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.when}</dd>
          <dt>
            <span id="vehicle">
              <Translate contentKey="rfidh2ElkApp.events.vehicle">Vehicle</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.vehicle}</dd>
          <dt>
            <span id="stolen">
              <Translate contentKey="rfidh2ElkApp.events.stolen">Stolen</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.stolen}</dd>
          <dt>
            <span id="wanted">
              <Translate contentKey="rfidh2ElkApp.events.wanted">Wanted</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.wanted ? 'true' : 'false'}</dd>
          <dt>
            <span id="gantryProcessed">
              <Translate contentKey="rfidh2ElkApp.events.gantryProcessed">Gantry Processed</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.gantryProcessed}</dd>
          <dt>
            <span id="gantrySent">
              <Translate contentKey="rfidh2ElkApp.events.gantrySent">Gantry Sent</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.gantrySent}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rfidh2ElkApp.events.status">Status</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.status}</dd>
          <dt>
            <span id="dataStatus">
              <Translate contentKey="rfidh2ElkApp.events.dataStatus">Data Status</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.dataStatus}</dd>
          <dt>
            <span id="threadRandNo">
              <Translate contentKey="rfidh2ElkApp.events.threadRandNo">Thread Rand No</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.threadRandNo}</dd>
          <dt>
            <span id="handledBy">
              <Translate contentKey="rfidh2ElkApp.events.handledBy">Handled By</Translate>
            </span>
          </dt>
          <dd>{eventsEntity.handledBy}</dd>
        </dl>
        <Button tag={Link} to="/events" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/events/${eventsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventsDetail;
