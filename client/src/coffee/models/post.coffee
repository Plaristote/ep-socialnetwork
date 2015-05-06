class Post extends Backbone.Model
  urlRoot: "/posts"

  get_reduced_url: () ->
    url = ''
    if @has 'url'
      url = @get 'url'
      url = url[0..29] + '...' if url.length > 32
    url
    
  created_at: () ->
    window.post = @
    date = @get 'at'
    year = (date.match /[0-9]{4}$/)[0]
    date = "#{date[0..19]} #{year}"
    moment @get('at'), 'ddd MMM DD HH:mm:ss YYYY'

  enabled: () ->
    if @has 'enable'
      @get 'enable'
    else
      true

  highlighted: () ->
    if @has 'highlight'
      @get 'highlight'
    else
      false

  can_be_seen_by_current_user: () ->
    @enabled() or @is_related_to_current_user()

  is_related_to_current_user: () ->
    current_user_id = application.current_user.get('id')
    (@get 'from') == current_user_id or (@get 'to') == current_user_id

  is_owned_by_current_user: () ->
    (@get 'to') == application.current_user.get('id')

  toggle_boolean_attribute: (attr_name, options) ->
    attribute = post.get attr_name
    attribute = if attribute == false then true else false
    post.set attr_name, attribute
    post.save {}, options
