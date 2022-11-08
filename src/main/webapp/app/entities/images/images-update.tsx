import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IImages } from 'app/shared/model/images.model';
import { getEntity, updateEntity, createEntity, reset } from './images.reducer';

export const ImagesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const imagesEntity = useAppSelector(state => state.images.entity);
  const loading = useAppSelector(state => state.images.loading);
  const updating = useAppSelector(state => state.images.updating);
  const updateSuccess = useAppSelector(state => state.images.updateSuccess);

  const handleClose = () => {
    navigate('/images' + location.search);
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
      ...imagesEntity,
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
          ...imagesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rfidh2ElkApp.images.home.createOrEditLabel" data-cy="ImagesCreateUpdateHeading">
            <Translate contentKey="rfidh2ElkApp.images.home.createOrEditLabel">Create or edit a Images</Translate>
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
                  id="images-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rfidh2ElkApp.images.guid')}
                id="images-guid"
                name="guid"
                data-cy="guid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.images.plate')} id="images-plate" name="plate" data-cy="plate" type="text" />
              <ValidatedBlobField
                label={translate('rfidh2ElkApp.images.imageLp')}
                id="images-imageLp"
                name="imageLp"
                data-cy="imageLp"
                isImage
                accept="image/*"
              />
              <ValidatedField label={translate('rfidh2ElkApp.images.anpr')} id="images-anpr" name="anpr" data-cy="anpr" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.images.rfid')} id="images-rfid" name="rfid" data-cy="rfid" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.dataStatus')}
                id="images-dataStatus"
                name="dataStatus"
                data-cy="dataStatus"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.gantry')}
                id="images-gantry"
                name="gantry"
                data-cy="gantry"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.lane')}
                id="images-lane"
                name="lane"
                data-cy="lane"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.images.kph')} id="images-kph" name="kph" data-cy="kph" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.ambush')}
                id="images-ambush"
                name="ambush"
                data-cy="ambush"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.direction')}
                id="images-direction"
                name="direction"
                data-cy="direction"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.vehicle')}
                id="images-vehicle"
                name="vehicle"
                data-cy="vehicle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.images.issue')} id="images-issue" name="issue" data-cy="issue" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.images.status')}
                id="images-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/images" replace color="info">
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

export default ImagesUpdate;
