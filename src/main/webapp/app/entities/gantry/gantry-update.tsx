import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGantry } from 'app/shared/model/gantry.model';
import { getEntity, updateEntity, createEntity, reset } from './gantry.reducer';

export const GantryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gantryEntity = useAppSelector(state => state.gantry.entity);
  const loading = useAppSelector(state => state.gantry.loading);
  const updating = useAppSelector(state => state.gantry.updating);
  const updateSuccess = useAppSelector(state => state.gantry.updateSuccess);

  const handleClose = () => {
    navigate('/gantry' + location.search);
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
      ...gantryEntity,
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
          ...gantryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rfidh2ElkApp.gantry.home.createOrEditLabel" data-cy="GantryCreateUpdateHeading">
            <Translate contentKey="rfidh2ElkApp.gantry.home.createOrEditLabel">Create or edit a Gantry</Translate>
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
                  id="gantry-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rfidh2ElkApp.gantry.guid')}
                id="gantry-guid"
                name="guid"
                data-cy="guid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.gantry.nameEn')}
                id="gantry-nameEn"
                name="nameEn"
                data-cy="nameEn"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.gantry.nameAr')}
                id="gantry-nameAr"
                name="nameAr"
                data-cy="nameAr"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('rfidh2ElkApp.gantry.status')}
                id="gantry-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <ValidatedField label={translate('rfidh2ElkApp.gantry.code')} id="gantry-code" name="code" data-cy="code" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gantry" replace color="info">
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

export default GantryUpdate;
