$(document).ready(function() {
   alert('loan js ready')
});
function() {
      var $form = $('#customerLoanForm');
      $form.on('submit', function(e) {
        e.preventDefault();
        $.ajax({
          url: $form.attr('action'),
          type: 'POST',
          headers: {
            "Authorization": localStorage.getItem('JwtToken')
           },
          data: $form.serialize(),
          success: function(response) {

          }
        });
}