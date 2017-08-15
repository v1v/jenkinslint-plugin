Behaviour.specify("#filter-box", '_table', 0, function(e) {
      function applyFilter() {
          var filter = e.value.toLowerCase();
          ["jenkinsLintTable","jenkinsSlaveTable"].each(function(clz) {
              // Declare variables
              var input, filter, table, tr, td, i;
              input = document.getElementById("filter-box");
              filter = input.value.toUpperCase();
              table = document.getElementById(clz);
              tr = table.getElementsByTagName("tr");

              // Loop through all table rows, and hide those who don't match the search query
              for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[0];
                if (td) {
                  if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                  } else {
                    tr[i].style.display = "none";
                  }
                }
              }
          });

          layoutUpdateCallback.call();
      }

      e.onkeyup = applyFilter;
});
