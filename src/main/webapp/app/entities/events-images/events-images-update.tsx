import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEventsImages } from 'app/shared/model/events-images.model';
import { getEntity, updateEntity, createEntity, reset } from './events-images.reducer';

export const EventsImagesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const eventsImagesEntity = useAppSelector(state => state.eventsImages.entity);
  const loading = useAppSelector(state => state.eventsImages.loading);
  const updating = useAppSelector(state => state.eventsImages.updating);
  const updateSuccess = useAppSelector(state => state.eventsImages.updateSuccess);

  const handleClose = () => {
    navigate('/events-images' + location.search);
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
      ...eventsImagesEntity,
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
          ...eventsImagesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rfidh2ElkApp.eventsImages.home.createOrEditLabel" data-cy="EventsImagesCreateUpdateHeading">
            <Translate contentKey="rfidh2ElkApp.eventsImages.home.createOrEditLabel">Create or edit a EventsImages</Translate>
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
                  id="events-images-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.guid')}
                id="events-images-guid"
                name="guid"
                data-cy="guid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.imageLp')}
                id="events-images-imageLp"
                name="imageLp"
                data-cy="imageLp"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.imageThumb')}
                id="events-images-imageThumb"
                name="imageThumb"
                data-cy="imageThumb"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.processingTime')}
                id="events-images-processingTime"
                name="processingTime"
                data-cy="processingTime"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.ruleRcvd')}
                id="events-images-ruleRcvd"
                name="ruleRcvd"
                data-cy="ruleRcvd"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.ruleSent')}
                id="events-images-ruleSent"
                name="ruleSent"
                data-cy="ruleSent"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.when')}
                id="events-images-when"
                name="when"
                data-cy="when"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.gantryProcessed')}
                id="events-images-gantryProcessed"
                name="gantryProcessed"
                data-cy="gantryProcessed"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.gantrySent')}
                id="events-images-gantrySent"
                name="gantrySent"
                data-cy="gantrySent"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.status')}
                id="events-images-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.dataStatus')}
                id="events-images-dataStatus"
                name="dataStatus"
                data-cy="dataStatus"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.threadRandNo')}
                id="events-images-threadRandNo"
                name="threadRandNo"
                data-cy="threadRandNo"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.eventsImages.gantry')}
                id="events-images-gantry"
                name="gantry"
                data-cy="gantry"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/events-images" replace color="info">
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

export default EventsImagesUpdate;
