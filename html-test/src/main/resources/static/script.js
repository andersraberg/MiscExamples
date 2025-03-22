document.addEventListener("DOMContentLoaded", function () {
    function toggle(event) {
        event.stopPropagation();
        let parent = event.target.closest("li");
        let children = parent.querySelector(".nested");
        let icon = parent.querySelector(".toggle-icon");
        if (children) {
            children.classList.toggle("active");
            icon.textContent = children.classList.contains("active") ? "-" : "+";
        }
    }

    function filterList() {
        let searchValue = document.getElementById("searchInput").value.toLowerCase();
        let items = document.querySelectorAll("li");

        items.forEach((item) => {
            let text = item.textContent.toLowerCase();
            let match = text.includes(searchValue);

            if (match) {
                item.style.display = "block";
                expandParents(item);
            } else {
                item.style.display = "none";
            }
        });
    }

    function expandParents(item) {
        let parentUl = item.closest("ul.nested");
        while (parentUl) {
            parentUl.classList.add("active");
            let parentLi = parentUl.closest("li");
            if (parentLi) {
                let icon = parentLi.querySelector(".toggle-icon");
                if (icon) icon.textContent = "-";
            }
            parentUl = parentLi ? parentLi.closest("ul.nested") : null;
        }
    }

    function expandAll(expand) {
        let nestedLists = document.querySelectorAll(".nested");
        nestedLists.forEach((nested) => {
            if (expand) {
                nested.classList.add("active");
                let parentIcon = nested.closest("li").querySelector(".toggle-icon");
                if (parentIcon) parentIcon.textContent = "-";
            } else {
                nested.classList.remove("active");
                let parentIcon = nested.closest("li").querySelector(".toggle-icon");
                if (parentIcon) parentIcon.textContent = "+";
            }
        });
    }

    document.querySelectorAll(".toggle-icon").forEach((icon) => {
        icon.addEventListener("click", toggle);
    });

    document.getElementById("searchInput").addEventListener("keyup", filterList);

    window.filterList = filterList;
    window.expandAll = expandAll;
});
