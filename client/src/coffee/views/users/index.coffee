class UsersView extends View
  name: 'inner-view'
  template:      JST['user_index']
  template_user: JST['user_index_entry']
  events:
    'click .user-item .show': 'show_user'

  constructor: (collection) ->
    @collection = collection
    @listenTo @collection, "add", @add_page.bind(@)
    super null

  render: () ->
    @$el.html @template()
    super

  contains_user: (user_id) ->
    @$(".user-item[data-id='#{user_id}']").length != 0

  add_page: () ->
    for model in @collection.models
      @add_user model unless @contains_user model.get('id')

  add_user: (user) ->
    html = @template_user user: user
    @$(".user-list").append html
    
  show_user: (e) ->
    $user_item = $(e.currentTarget).closest('.user-item')
    user_id    = $user_item.data 'id'
    Backbone.history.navigate "/users/#{user_id}", trigger: true