import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEventsImages } from 'app/shared/model/events-images.model';
import { searchEntities, getEntities } from './events-images.reducer';

export const EventsImages = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const eventsImagesList = useAppSelector(state => state.eventsImages.entities);
  const loading = useAppSelector(state => state.eventsImages.loading);
  const totalItems = useAppSelector(state => state.eventsImages.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="events-images-heading" data-cy="EventsImagesHeading">
        <Translate contentKey="rfidh2ElkApp.eventsImages.home.title">Events Images</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rfidh2ElkApp.eventsImages.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/events-images/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rfidh2ElkApp.eventsImages.home.createLabel">Create new Events Images</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('rfidh2ElkApp.eventsImages.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {eventsImagesList && eventsImagesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('guid')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.guid">Guid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imageLp')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.imageLp">Image Lp</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imageThumb')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.imageThumb">Image Thumb</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('processingTime')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.processingTime">Processing Time</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleRcvd')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.ruleRcvd">Rule Rcvd</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ruleSent')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.ruleSent">Rule Sent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('when')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.when">When</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantryProcessed')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.gantryProcessed">Gantry Processed</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantrySent')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.gantrySent">Gantry Sent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dataStatus')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.dataStatus">Data Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('threadRandNo')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.threadRandNo">Thread Rand No</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gantry')}>
                  <Translate contentKey="rfidh2ElkApp.eventsImages.gantry">Gantry</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {eventsImagesList.map((eventsImages, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/events-images/${eventsImages.id}`} color="link" size="sm">
                      {eventsImages.id}
                    </Button>
                  </td>
                  <td>{eventsImages.guid}</td>
                  <td>{eventsImages.imageLp}</td>
                  <td>{eventsImages.imageThumb}</td>
                  <td>{eventsImages.processingTime}</td>
                  <td>{eventsImages.ruleRcvd}</td>
                  <td>{eventsImages.ruleSent}</td>
                  <td>{eventsImages.when}</td>
                  <td>{eventsImages.gantryProcessed}</td>
                  <td>{eventsImages.gantrySent}</td>
                  <td>{eventsImages.status}</td>
                  <td>{eventsImages.dataStatus}</td>
                  <td>{eventsImages.threadRandNo}</td>
                  <td>{eventsImages.gantry}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/events-images/${eventsImages.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/events-images/${eventsImages.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/events-images/${eventsImages.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="rfidh2ElkApp.eventsImages.home.notFound">No Events Images found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={eventsImagesList && eventsImagesList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default EventsImages;
