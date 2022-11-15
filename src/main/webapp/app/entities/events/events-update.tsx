import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEvents } from 'app/shared/model/events.model';
import { getEntity, updateEntity, createEntity, reset } from './events.reducer';

export const EventsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const eventsEntity = useAppSelector(state => state.events.entity);
  const loading = useAppSelector(state => state.events.loading);
  const updating = useAppSelector(state => state.events.updating);
  const updateSuccess = useAppSelector(state => state.events.updateSuccess);

  const handleClose = () => {
    navigate('/events' + location.search);
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
      ...eventsEntity,
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
          ...eventsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rfidh2ElkApp.events.home.createOrEditLabel" data-cy="EventsCreateUpdateHeading">
            <Translate contentKey="rfidh2ElkApp.events.home.createOrEditLabel">Create or edit a Events</Translate>
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
                  id="events-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rfidh2ElkApp.events.guid')}
                id="events-guid"
                name="guid"
                data-cy="guid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.events.plate')} id="events-plate" name="plate" data-cy="plate" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.events.anpr')} id="events-anpr" name="anpr" data-cy="anpr" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.events.rfid')} id="events-rfid" name="rfid" data-cy="rfid" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.dataStatus')}
                id="events-dataStatus"
                name="dataStatus"
                data-cy="dataStatus"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.gantry')}
                id="events-gantry"
                name="gantry"
                data-cy="gantry"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.events.kph')} id="events-kph" name="kph" data-cy="kph" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.ambush')}
                id="events-ambush"
                name="ambush"
                data-cy="ambush"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.direction')}
                id="events-direction"
                name="direction"
                data-cy="direction"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.vehicle')}
                id="events-vehicle"
                name="vehicle"
                data-cy="vehicle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.events.issue')} id="events-issue" name="issue" data-cy="issue" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.status')}
                id="events-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.handledBy')}
                id="events-handledBy"
                name="handledBy"
                data-cy="handledBy"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.gantryProcessed')}
                id="events-gantryProcessed"
                name="gantryProcessed"
                data-cy="gantryProcessed"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.gantrySent')}
                id="events-gantrySent"
                name="gantrySent"
                data-cy="gantrySent"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.events.when')} id="events-when" name="when" data-cy="when" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.events.toll')} id="events-toll" name="toll" data-cy="toll" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.ruleRcvd')}
                id="events-ruleRcvd"
                name="ruleRcvd"
                data-cy="ruleRcvd"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.wantedFor')}
                id="events-wantedFor"
                name="wantedFor"
                data-cy="wantedFor"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.events.fine')} id="events-fine" name="fine" data-cy="fine" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.licenseIssue')}
                id="events-licenseIssue"
                name="licenseIssue"
                data-cy="licenseIssue"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.wantedBy')}
                id="events-wantedBy"
                name="wantedBy"
                data-cy="wantedBy"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.ruleProcessed')}
                id="events-ruleProcessed"
                name="ruleProcessed"
                data-cy="ruleProcessed"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.speedFine')}
                id="events-speedFine"
                name="speedFine"
                data-cy="speedFine"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.lane')}
                id="events-lane"
                name="lane"
                data-cy="lane"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.tagIssue')}
                id="events-tagIssue"
                name="tagIssue"
                data-cy="tagIssue"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.statusName')}
                id="events-statusName"
                name="statusName"
                data-cy="statusName"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.licenseFine')}
                id="events-licenseFine"
                name="licenseFine"
                data-cy="licenseFine"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.stolen')}
                id="events-stolen"
                name="stolen"
                data-cy="stolen"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.wanted')}
                id="events-wanted"
                name="wanted"
                data-cy="wanted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.ruleSent')}
                id="events-ruleSent"
                name="ruleSent"
                data-cy="ruleSent"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.handled')}
                id="events-handled"
                name="handled"
                data-cy="handled"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.events.ruleIssue')}
                id="events-ruleIssue"
                name="ruleIssue"
                data-cy="ruleIssue"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/events" replace color="info">
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

export default EventsUpdate;
