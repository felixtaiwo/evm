import { adminRoot } from './defaultValues';

const data = [{
        id: 'gogo',
        icon: 'iconsminds-air-balloon-1',
        label: 'menu.gogo',
        to: `${adminRoot}/admin`,
        subs: [{
                icon: 'simple-icon-paper-plane',
                label: 'menu.start',
                to: `${adminRoot}/admin/dashboard`,
            },
            {
                icon: 'simple-icon-paper-plane',
                label: 'menu.start2',
                to: `${adminRoot}/admin/results`,
            },
        ],
    },
    {
        id: 'Student',
        icon: 'iconsminds-three-arrow-fork',
        label: 'menu.second-menu',
        to: `${adminRoot}/user`,
        // roles: [UserRole.Admin, UserRole.Editor],
        subs: [{
                icon: 'simple-icon-paper-plane',
                label: 'menu.second1',
                to: `${adminRoot}/user/dashboard`,
            },
            {
                icon: 'simple-icon-paper-plane',
                label: 'menu.second2',
                to: `${adminRoot}/user/polls`,
            },
            {
                icon: 'simple-icon-paper-plane',
                label: 'menu.second3',
                to: `${adminRoot}/user/account`,
            },
        ],
    },
    {
        id: 'blankpage',
        icon: 'iconsminds-bucket',
        label: 'menu.blank-page',
        to: `${adminRoot}/operator`,
        subs: [{
            icon: 'simple-icon-paper-plane',
            label: 'menu.second3',
            to: `${adminRoot}/operator/dashboard`,
        }],
    }
];
export default data;