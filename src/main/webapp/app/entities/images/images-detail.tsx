import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './images.reducer';

export const ImagesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imagesEntity = useAppSelector(state => state.images.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imagesDetailsHeading">
          <Translate contentKey="rfidh2ElkApp.images.detail.title">Images</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.id}</dd>
          <dt>
            <span id="guid">
              <Translate contentKey="rfidh2ElkApp.images.guid">Guid</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.guid}</dd>
          <dt>
            <span id="plate">
              <Translate contentKey="rfidh2ElkApp.images.plate">Plate</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.plate}</dd>
          <dt>
            <span id="anpr">
              <Translate contentKey="rfidh2ElkApp.images.anpr">Anpr</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.anpr}</dd>
          <dt>
            <span id="rfid">
              <Translate contentKey="rfidh2ElkApp.images.rfid">Rfid</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.rfid}</dd>
          <dt>
            <span id="dataStatus">
              <Translate contentKey="rfidh2ElkApp.images.dataStatus">Data Status</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.dataStatus}</dd>
          <dt>
            <span id="gantry">
              <Translate contentKey="rfidh2ElkApp.images.gantry">Gantry</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.gantry}</dd>
          <dt>
            <span id="lane">
              <Translate contentKey="rfidh2ElkApp.images.lane">Lane</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.lane}</dd>
          <dt>
            <span id="kph">
              <Translate contentKey="rfidh2ElkApp.images.kph">Kph</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.kph}</dd>
          <dt>
            <span id="ambush">
              <Translate contentKey="rfidh2ElkApp.images.ambush">Ambush</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.ambush}</dd>
          <dt>
            <span id="direction">
              <Translate contentKey="rfidh2ElkApp.images.direction">Direction</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.direction}</dd>
          <dt>
            <span id="vehicle">
              <Translate contentKey="rfidh2ElkApp.images.vehicle">Vehicle</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.vehicle}</dd>
          <dt>
            <span id="imageLp">
              <Translate contentKey="rfidh2ElkApp.images.imageLp">Image Lp</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.imageLp}</dd>
          <dt>
            <span id="issue">
              <Translate contentKey="rfidh2ElkApp.images.issue">Issue</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.issue}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rfidh2ElkApp.images.status">Status</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/images" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/images/${imagesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImagesDetail;
