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
