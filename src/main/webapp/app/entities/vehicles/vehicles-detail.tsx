import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicles.reducer';

export const VehiclesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehiclesEntity = useAppSelector(state => state.vehicles.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehiclesDetailsHeading">
          <Translate contentKey="rfidh2ElkApp.vehicles.detail.title">Vehicles</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.id}</dd>
          <dt>
            <span id="guid">
              <Translate contentKey="rfidh2ElkApp.vehicles.guid">Guid</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.guid}</dd>
          <dt>
            <span id="plate">
              <Translate contentKey="rfidh2ElkApp.vehicles.plate">Plate</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.plate}</dd>
          <dt>
            <span id="anpr">
              <Translate contentKey="rfidh2ElkApp.vehicles.anpr">Anpr</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.anpr}</dd>
          <dt>
            <span id="rfid">
              <Translate contentKey="rfidh2ElkApp.vehicles.rfid">Rfid</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.rfid}</dd>
          <dt>
            <span id="dataStatus">
              <Translate contentKey="rfidh2ElkApp.vehicles.dataStatus">Data Status</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.dataStatus}</dd>
          <dt>
            <span id="gantry">
              <Translate contentKey="rfidh2ElkApp.vehicles.gantry">Gantry</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.gantry}</dd>
          <dt>
            <span id="lane">
              <Translate contentKey="rfidh2ElkApp.vehicles.lane">Lane</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.lane}</dd>
          <dt>
            <span id="kph">
              <Translate contentKey="rfidh2ElkApp.vehicles.kph">Kph</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.kph}</dd>
          <dt>
            <span id="ambush">
              <Translate contentKey="rfidh2ElkApp.vehicles.ambush">Ambush</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.ambush}</dd>
          <dt>
            <span id="direction">
              <Translate contentKey="rfidh2ElkApp.vehicles.direction">Direction</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.direction}</dd>
          <dt>
            <span id="vehicle">
              <Translate contentKey="rfidh2ElkApp.vehicles.vehicle">Vehicle</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.vehicle}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rfidh2ElkApp.vehicles.status">Status</Translate>
            </span>
          </dt>
          <dd>{vehiclesEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/vehicles" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicles/${vehiclesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehiclesDetail;
