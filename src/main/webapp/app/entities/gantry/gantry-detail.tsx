import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './gantry.reducer';

export const GantryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gantryEntity = useAppSelector(state => state.gantry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gantryDetailsHeading">
          <Translate contentKey="rfidh2ElkApp.gantry.detail.title">Gantry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gantryEntity.id}</dd>
          <dt>
            <span id="guid">
              <Translate contentKey="rfidh2ElkApp.gantry.guid">Guid</Translate>
            </span>
          </dt>
          <dd>{gantryEntity.guid}</dd>
          <dt>
            <span id="nameEn">
              <Translate contentKey="rfidh2ElkApp.gantry.nameEn">Name En</Translate>
            </span>
          </dt>
          <dd>{gantryEntity.nameEn}</dd>
          <dt>
            <span id="nameAr">
              <Translate contentKey="rfidh2ElkApp.gantry.nameAr">Name Ar</Translate>
            </span>
          </dt>
          <dd>{gantryEntity.nameAr}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rfidh2ElkApp.gantry.status">Status</Translate>
            </span>
          </dt>
          <dd>{gantryEntity.status}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="rfidh2ElkApp.gantry.code">Code</Translate>
            </span>
          </dt>
          <dd>{gantryEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/gantry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gantry/${gantryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GantryDetail;
