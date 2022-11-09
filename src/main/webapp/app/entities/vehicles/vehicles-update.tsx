import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicles } from 'app/shared/model/vehicles.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicles.reducer';

export const VehiclesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehiclesEntity = useAppSelector(state => state.vehicles.entity);
  const loading = useAppSelector(state => state.vehicles.loading);
  const updating = useAppSelector(state => state.vehicles.updating);
  const updateSuccess = useAppSelector(state => state.vehicles.updateSuccess);

  const handleClose = () => {
    navigate('/vehicles' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...vehiclesEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...vehiclesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rfidh2ElkApp.vehicles.home.createOrEditLabel" data-cy="VehiclesCreateUpdateHeading">
            <Translate contentKey="rfidh2ElkApp.vehicles.home.createOrEditLabel">Create or edit a Vehicles</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="vehicles-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.guid')}
                id="vehicles-guid"
                name="guid"
                data-cy="guid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.plate')}
                id="vehicles-plate"
                name="plate"
                data-cy="plate"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.vehicles.anpr')} id="vehicles-anpr" name="anpr" data-cy="anpr" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.vehicles.rfid')} id="vehicles-rfid" name="rfid" data-cy="rfid" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.dataStatus')}
                id="vehicles-dataStatus"
                name="dataStatus"
                data-cy="dataStatus"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.gantry')}
                id="vehicles-gantry"
                name="gantry"
                data-cy="gantry"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.lane')}
                id="vehicles-lane"
                name="lane"
                data-cy="lane"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.vehicles.kph')} id="vehicles-kph" name="kph" data-cy="kph" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.ambush')}
                id="vehicles-ambush"
                name="ambush"
                data-cy="ambush"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.direction')}
                id="vehicles-direction"
                name="direction"
                data-cy="direction"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.vehicle')}
                id="vehicles-vehicle"
                name="vehicle"
                data-cy="vehicle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.vehicles.status')}
                id="vehicles-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicles" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VehiclesUpdate;
