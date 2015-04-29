class UsersView extends View
  name: 'inner-view'
  template:      JST['user_index']
  template_user: JST['user_index_entry']
  events:
    'click .user-item .show':       'show_user'
    'click .user-item .add-friend': 'add_as_friend'
    'click .user-item .del-friend': 'delete_from_friends'
    'click #load-more':             'load_more'

  constructor: (collection) ->
    @collection = collection
    @listenTo @collection, "add", @add_page.bind(@)
    @listenTo application.current_user, "change", @current_user_changed.bind(@)
    super null

  render: () ->
    @$el.html @template()
    super

  contains_user: (user_id) ->
    @$(".user-item[data-id='#{user_id}']").length != 0

  load_more: () ->
    console.log @collection
    @$('#load-more').fadeOut()
    @collection.getNextPage().done =>
      @$('#load-more').fadeIn() unless @collection.is_at_last_page()

  add_page: () ->
    for model in @collection.models
      @add_user model unless @contains_user model.get('id')

  add_user: (user) ->
    html = @template_user user: user
    @$(".user-list").append html
    @current_user_changed()
  
  get_user_id_from_event: (event) ->
    $(event.currentTarget).closest('.user-item').data 'id'

  add_as_friend: (event) ->
    application.current_user.add_friend @get_user_id_from_event(event)

  delete_from_friends: (event) ->
    application.current_user.delete_friend @get_user_id_from_event(event)

  show_user: (event) ->
    user_id = @get_user_id_from_event(event)
    Backbone.history.navigate "/users/#{user_id}", trigger: true

  current_user_changed: () ->
    if application.current_user.has('id')
      @$(".user-item .add-friend").show()
      @$(".user-item .del-friend").hide()
      selector = (id) -> ".user-item[data-id='#{id}']"
      self_selector = selector(application.current_user.get 'id')
      @$("#{self_selector} .add-friend, #{self_selector} .del-friend").hide()
      if application.current_user.has('friends_ids')
        for id in application.current_user.get('friends_ids')
          @$("#{selector(id)} .add-friend").hide()
          @$("#{selector(id)} .del-friend").show()
    else
      @$('.add-friend, .del-friend').hide()