import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './events-images.reducer';

export const EventsImagesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventsImagesEntity = useAppSelector(state => state.eventsImages.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventsImagesDetailsHeading">
          <Translate contentKey="rfidh2ElkApp.eventsImages.detail.title">EventsImages</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.id}</dd>
          <dt>
            <span id="guid">
              <Translate contentKey="rfidh2ElkApp.eventsImages.guid">Guid</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.guid}</dd>
          <dt>
            <span id="imageLp">
              <Translate contentKey="rfidh2ElkApp.eventsImages.imageLp">Image Lp</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.imageLp}</dd>
          <dt>
            <span id="imageThumb">
              <Translate contentKey="rfidh2ElkApp.eventsImages.imageThumb">Image Thumb</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.imageThumb}</dd>
          <dt>
            <span id="processingTime">
              <Translate contentKey="rfidh2ElkApp.eventsImages.processingTime">Processing Time</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.processingTime}</dd>
          <dt>
            <span id="ruleRcvd">
              <Translate contentKey="rfidh2ElkApp.eventsImages.ruleRcvd">Rule Rcvd</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.ruleRcvd}</dd>
          <dt>
            <span id="ruleSent">
              <Translate contentKey="rfidh2ElkApp.eventsImages.ruleSent">Rule Sent</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.ruleSent}</dd>
          <dt>
            <span id="when">
              <Translate contentKey="rfidh2ElkApp.eventsImages.when">When</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.when}</dd>
          <dt>
            <span id="gantryProcessed">
              <Translate contentKey="rfidh2ElkApp.eventsImages.gantryProcessed">Gantry Processed</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.gantryProcessed}</dd>
          <dt>
            <span id="gantrySent">
              <Translate contentKey="rfidh2ElkApp.eventsImages.gantrySent">Gantry Sent</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.gantrySent}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rfidh2ElkApp.eventsImages.status">Status</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.status}</dd>
          <dt>
            <span id="dataStatus">
              <Translate contentKey="rfidh2ElkApp.eventsImages.dataStatus">Data Status</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.dataStatus}</dd>
          <dt>
            <span id="threadRandNo">
              <Translate contentKey="rfidh2ElkApp.eventsImages.threadRandNo">Thread Rand No</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.threadRandNo}</dd>
          <dt>
            <span id="gantry">
              <Translate contentKey="rfidh2ElkApp.eventsImages.gantry">Gantry</Translate>
            </span>
          </dt>
          <dd>{eventsImagesEntity.gantry}</dd>
        </dl>
        <Button tag={Link} to="/events-images" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/events-images/${eventsImagesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventsImagesDetail;
