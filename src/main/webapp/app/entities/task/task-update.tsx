import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITask } from 'app/shared/model/task.model';
import { getEntity, updateEntity, createEntity, reset } from './task.reducer';

export const TaskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const taskEntity = useAppSelector(state => state.task.entity);
  const loading = useAppSelector(state => state.task.loading);
  const updating = useAppSelector(state => state.task.updating);
  const updateSuccess = useAppSelector(state => state.task.updateSuccess);

  const handleClose = () => {
    navigate('/task' + location.search);
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
      ...taskEntity,
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
          ...taskEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rfidh2ElkApp.task.home.createOrEditLabel" data-cy="TaskCreateUpdateHeading">
            <Translate contentKey="rfidh2ElkApp.task.home.createOrEditLabel">Create or edit a Task</Translate>
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
                  id="task-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rfidh2ElkApp.task.guid')}
                id="task-guid"
                name="guid"
                data-cy="guid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.plate')} id="task-plate" name="plate" data-cy="plate" type="text" />
              <ValidatedBlobField
                label={translate('rfidh2ElkApp.task.imageLp')}
                id="task-imageLp"
                name="imageLp"
                data-cy="imageLp"
                isImage
                accept="image/*"
              />
              <ValidatedBlobField
                label={translate('rfidh2ElkApp.task.imageThumb')}
                id="task-imageThumb"
                name="imageThumb"
                data-cy="imageThumb"
                isImage
                accept="image/*"
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.anpr')} id="task-anpr" name="anpr" data-cy="anpr" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.task.rfid')} id="task-rfid" name="rfid" data-cy="rfid" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.dataStatus')}
                id="task-dataStatus"
                name="dataStatus"
                data-cy="dataStatus"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.gantry')}
                id="task-gantry"
                name="gantry"
                data-cy="gantry"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.kph')} id="task-kph" name="kph" data-cy="kph" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.task.ambush')} id="task-ambush" name="ambush" data-cy="ambush" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.direction')}
                id="task-direction"
                name="direction"
                data-cy="direction"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.vehicle')}
                id="task-vehicle"
                name="vehicle"
                data-cy="vehicle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.issue')} id="task-issue" name="issue" data-cy="issue" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.task.status')} id="task-status" name="status" data-cy="status" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.handledBy')}
                id="task-handledBy"
                name="handledBy"
                data-cy="handledBy"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.gantryProcessed')}
                id="task-gantryProcessed"
                name="gantryProcessed"
                data-cy="gantryProcessed"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.gantrySent')}
                id="task-gantrySent"
                name="gantrySent"
                data-cy="gantrySent"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.when')} id="task-when" name="when" data-cy="when" type="text" />
              <ValidatedField label={translate('rfidh2ElkApp.task.toll')} id="task-toll" name="toll" data-cy="toll" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.ruleRcvd')}
                id="task-ruleRcvd"
                name="ruleRcvd"
                data-cy="ruleRcvd"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.wantedFor')}
                id="task-wantedFor"
                name="wantedFor"
                data-cy="wantedFor"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.fine')} id="task-fine" name="fine" data-cy="fine" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.licenseIssue')}
                id="task-licenseIssue"
                name="licenseIssue"
                data-cy="licenseIssue"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.wantedBy')}
                id="task-wantedBy"
                name="wantedBy"
                data-cy="wantedBy"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.ruleProcessed')}
                id="task-ruleProcessed"
                name="ruleProcessed"
                data-cy="ruleProcessed"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.speedFine')}
                id="task-speedFine"
                name="speedFine"
                data-cy="speedFine"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.lane')}
                id="task-lane"
                name="lane"
                data-cy="lane"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.tagIssue')}
                id="task-tagIssue"
                name="tagIssue"
                data-cy="tagIssue"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.statusName')}
                id="task-statusName"
                name="statusName"
                data-cy="statusName"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.licenseFine')}
                id="task-licenseFine"
                name="licenseFine"
                data-cy="licenseFine"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.task.stolen')} id="task-stolen" name="stolen" data-cy="stolen" type="text" />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.wanted')}
                id="task-wanted"
                name="wanted"
                data-cy="wanted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.ruleSent')}
                id="task-ruleSent"
                name="ruleSent"
                data-cy="ruleSent"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.handled')}
                id="task-handled"
                name="handled"
                data-cy="handled"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.ruleIssue')}
                id="task-ruleIssue"
                name="ruleIssue"
                data-cy="ruleIssue"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.processingTime')}
                id="task-processingTime"
                name="processingTime"
                data-cy="processingTime"
                type="text"
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.task.threadRandNo')}
                id="task-threadRandNo"
                name="threadRandNo"
                data-cy="threadRandNo"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/task" replace color="info">
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

export default TaskUpdate;
